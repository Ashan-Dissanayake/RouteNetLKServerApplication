package lk.ashan.routenetlkserverapllication.module.user.validation;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.ChangePasswordRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.ResetPasswordRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateCreate(UserCreateRequestDto request) {
        validateUsernameUniqueness(request.getUsername());
        validateEmployeeDoesNotHaveUser(request.getEmployee().getId());
    }

    public void validateUpdate(User existingUser, UserUpdateRequestDto request) {
        validateEmployeeNotResigned(existingUser);
        validateUsernameUniquenessOnUpdate(existingUser, request.getUsername());
    }

    public void validateActivation(User user, Boolean accountLocked) {
        String employeeStatus = user.getEmployee()
                .getEmployeestatus()
                .getName();

        boolean lockAccount = Boolean.TRUE.equals(accountLocked);

        if ("RESIGNED".equalsIgnoreCase(employeeStatus)
                && !lockAccount) {

            throw new ResourceExistsException(
                    "Cannot activate user. Employee has resigned"
            );
        }
    }

    public void validateChangePassword(User user, ChangePasswordRequestDto request) {
        validateCurrentPassword(request.getCurrentPassword(), user.getPassword());
        validateNewPasswordIsDifferent(request.getNewPassword(), user.getPassword());
    }

    public void validateResetPassword(User user, ResetPasswordRequestDto request) {
        validateNewPasswordIsDifferent(request.getNewPassword(), user.getPassword());
    }

    private void validateUsernameUniqueness(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new ResourceExistsException(
                    "User with name : " + username + " already exists"
            );
        }
    }

    private void validateUsernameUniquenessOnUpdate(User existingUser, String username) {
        if (!existingUser.getUsername().equals(username) && userRepository.existsByUsernameAndIdNot(
                username,
                existingUser.getId())) {

            throw new ResourceExistsException(
                    "Username already exists"
            );
        }
    }

    private void validateEmployeeDoesNotHaveUser(Integer employeeId) {
        if (userRepository.existsByEmployee_Id(employeeId)) {
            throw new ResourceExistsException(
                    "Employee already has a user account"
            );
        }
    }

    private void validateEmployeeNotResigned(User user) {
        String employeeStatus = user.getEmployee().getEmployeestatus().getName();

        if ("RESIGNED".equalsIgnoreCase(employeeStatus)) {
            throw new ResourceExistsException(
                    "Cannot update user. Employee has resigned"
            );
        }
    }

    private void validateCurrentPassword(String currentPassword, String encodedPassword) {
        if (!passwordEncoder.matches(currentPassword, encodedPassword)) {
            throw new ResourceExistsException(
                    "Current password is incorrect"
            );
        }
    }

    private void validateNewPasswordIsDifferent(String newPassword, String encodedPassword) {
        if (passwordEncoder.matches(newPassword, encodedPassword)) {
            throw new ResourceExistsException(
                    "New password cannot be same as current password"
            );
        }
    }
}