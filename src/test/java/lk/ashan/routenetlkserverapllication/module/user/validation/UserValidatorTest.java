package lk.ashan.routenetlkserverapllication.module.user.validation;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.ChangePasswordRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.ResetPasswordRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserValidator validator;


    // =========================================================
    // validateCreate()
    // =========================================================

    @Test
    void validateCreate_shouldThrowExceptionWhenUsernameAlreadyExists() {

        String username = "ashan";

        UserCreateRequestDto request =
                mock(UserCreateRequestDto.class, RETURNS_DEEP_STUBS);

        when(request.getUsername())
                .thenReturn(username);

        when(userRepository.existsByUsername(username))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(userRepository)
                .existsByUsername(username);

        verify(userRepository, never())
                .existsByEmployee_Id(any());
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenEmployeeAlreadyHasUser() {

        String username = "ashan";
        Integer employeeId = 10;

        UserCreateRequestDto request =
                mock(UserCreateRequestDto.class, RETURNS_DEEP_STUBS);

        when(request.getUsername())
                .thenReturn(username);

        when(request.getEmployee().getId())
                .thenReturn(employeeId);

        when(userRepository.existsByUsername(username))
                .thenReturn(false);

        when(userRepository.existsByEmployee_Id(employeeId))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(userRepository)
                .existsByUsername(username);

        verify(userRepository)
                .existsByEmployee_Id(employeeId);
    }

    @Test
    void validateCreate_shouldPassWhenUsernameAndEmployeeAreValid() {

        String username = "ashan";
        Integer employeeId = 10;

        UserCreateRequestDto request =
                mock(UserCreateRequestDto.class, RETURNS_DEEP_STUBS);

        when(request.getUsername())
                .thenReturn(username);

        when(request.getEmployee().getId())
                .thenReturn(employeeId);

        when(userRepository.existsByUsername(username))
                .thenReturn(false);

        when(userRepository.existsByEmployee_Id(employeeId))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );

        verify(userRepository)
                .existsByUsername(username);

        verify(userRepository)
                .existsByEmployee_Id(employeeId);
    }


    // =========================================================
    // validateUpdate()
    // =========================================================

    @Test
    void validateUpdate_shouldThrowExceptionWhenEmployeeIsResigned() {

        User existingUser =
                mock(User.class, RETURNS_DEEP_STUBS);

        UserUpdateRequestDto request =
                mock(UserUpdateRequestDto.class);

        when(existingUser.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("RESIGNED");

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(existingUser, request)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenUsernameRemainsUnchanged() {

        String username = "ashan";

        User existingUser =
                mock(User.class, RETURNS_DEEP_STUBS);

        UserUpdateRequestDto request =
                mock(UserUpdateRequestDto.class);

        when(existingUser.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("ACTIVE");

        when(existingUser.getUsername())
                .thenReturn(username);

        when(request.getUsername())
                .thenReturn(username);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingUser, request)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenChangedUsernameAlreadyExists() {

        String currentUsername = "ashan";
        String newUsername = "kasun";
        Integer userId = 1;

        User existingUser =
                mock(User.class, RETURNS_DEEP_STUBS);

        UserUpdateRequestDto request =
                mock(UserUpdateRequestDto.class);

        when(existingUser.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("ACTIVE");

        when(existingUser.getUsername())
                .thenReturn(currentUsername);

        when(existingUser.getId())
                .thenReturn(userId);

        when(request.getUsername())
                .thenReturn(newUsername);

        when(userRepository.existsByUsernameAndIdNot(
                newUsername,
                userId
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(existingUser, request)
        );

        verify(userRepository)
                .existsByUsernameAndIdNot(
                        newUsername,
                        userId
                );
    }

    @Test
    void validateUpdate_shouldPassWhenChangedUsernameDoesNotExist() {

        String currentUsername = "ashan";
        String newUsername = "kasun";
        Integer userId = 1;

        User existingUser =
                mock(User.class, RETURNS_DEEP_STUBS);

        UserUpdateRequestDto request =
                mock(UserUpdateRequestDto.class);

        when(existingUser.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("ACTIVE");

        when(existingUser.getUsername())
                .thenReturn(currentUsername);

        when(existingUser.getId())
                .thenReturn(userId);

        when(request.getUsername())
                .thenReturn(newUsername);

        when(userRepository.existsByUsernameAndIdNot(
                newUsername,
                userId
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existingUser, request)
        );

        verify(userRepository)
                .existsByUsernameAndIdNot(
                        newUsername,
                        userId
                );
    }


    // =========================================================
    // validateActivation()
    // =========================================================

    @Test
    void validateActivation_shouldThrowExceptionWhenResignedEmployeeIsActivated() {

        User user =
                mock(User.class, RETURNS_DEEP_STUBS);

        when(user.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("RESIGNED");

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateActivation(user, false)
        );

        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void validateActivation_shouldPassWhenResignedEmployeeAccountRemainsLocked() {

        User user =
                mock(User.class, RETURNS_DEEP_STUBS);

        when(user.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("RESIGNED");

        assertDoesNotThrow(
                () -> validator.validateActivation(user, true)
        );
    }

    @Test
    void validateActivation_shouldPassWhenActiveEmployeeIsActivated() {

        User user =
                mock(User.class, RETURNS_DEEP_STUBS);

        when(user.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("ACTIVE");

        assertDoesNotThrow(
                () -> validator.validateActivation(user, false)
        );
    }

    @Test
    void validateActivation_shouldThrowExceptionWhenEmployeeStatusIsCaseInsensitiveResigned() {

        User user =
                mock(User.class, RETURNS_DEEP_STUBS);

        when(user.getEmployee()
                .getEmployeestatus()
                .getName())
                .thenReturn("resigned");

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateActivation(user, false)
        );
    }


    // =========================================================
    // validateChangePassword()
    // =========================================================

    @Test
    void validateChangePassword_shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {

        String currentPassword = "wrong-password";
        String encodedPassword = "encoded-password";

        User user = mock(User.class);

        ChangePasswordRequestDto request =
                mock(ChangePasswordRequestDto.class);

        when(request.getCurrentPassword())
                .thenReturn(currentPassword);

        when(user.getPassword())
                .thenReturn(encodedPassword);

        when(passwordEncoder.matches(
                currentPassword,
                encodedPassword
        )).thenReturn(false);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateChangePassword(user, request)
        );

        verify(passwordEncoder, times(1))
                .matches(currentPassword, encodedPassword);
    }

    @Test
    void validateChangePassword_shouldThrowExceptionWhenNewPasswordIsSameAsCurrentPassword() {

        String currentPassword = "current-password";
        String newPassword = "current-password";
        String encodedPassword = "encoded-password";

        User user = mock(User.class);

        ChangePasswordRequestDto request =
                mock(ChangePasswordRequestDto.class);

        when(request.getCurrentPassword())
                .thenReturn(currentPassword);

        when(request.getNewPassword())
                .thenReturn(newPassword);

        when(user.getPassword())
                .thenReturn(encodedPassword);

        when(passwordEncoder.matches(
                currentPassword,
                encodedPassword
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateChangePassword(user, request)
        );

        verify(passwordEncoder, times(2))
                .matches(currentPassword, encodedPassword);
    }

    @Test
    void validateChangePassword_shouldPassWhenCurrentPasswordIsCorrectAndNewPasswordIsDifferent() {

        String currentPassword = "current-password";
        String newPassword = "new-password";
        String encodedPassword = "encoded-password";

        User user =
                mock(User.class);

        ChangePasswordRequestDto request =
                mock(ChangePasswordRequestDto.class);

        when(request.getCurrentPassword())
                .thenReturn(currentPassword);

        when(request.getNewPassword())
                .thenReturn(newPassword);

        when(user.getPassword())
                .thenReturn(encodedPassword);

        when(passwordEncoder.matches(
                currentPassword,
                encodedPassword
        )).thenReturn(true);

        when(passwordEncoder.matches(
                newPassword,
                encodedPassword
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateChangePassword(user, request)
        );

        verify(passwordEncoder)
                .matches(currentPassword, encodedPassword);

        verify(passwordEncoder)
                .matches(newPassword, encodedPassword);
    }


    // =========================================================
    // validateResetPassword()
    // =========================================================

    @Test
    void validateResetPassword_shouldThrowExceptionWhenNewPasswordIsSameAsCurrentPassword() {

        String newPassword = "current-password";
        String encodedPassword = "encoded-password";

        User user =
                mock(User.class);

        ResetPasswordRequestDto request =
                mock(ResetPasswordRequestDto.class);

        when(request.getNewPassword())
                .thenReturn(newPassword);

        when(user.getPassword())
                .thenReturn(encodedPassword);

        when(passwordEncoder.matches(
                newPassword,
                encodedPassword
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateResetPassword(user, request)
        );

        verify(passwordEncoder)
                .matches(newPassword, encodedPassword);
    }

    @Test
    void validateResetPassword_shouldPassWhenNewPasswordIsDifferent() {

        String newPassword = "new-password";
        String encodedPassword = "encoded-password";

        User user =
                mock(User.class);

        ResetPasswordRequestDto request =
                mock(ResetPasswordRequestDto.class);

        when(request.getNewPassword())
                .thenReturn(newPassword);

        when(user.getPassword())
                .thenReturn(encodedPassword);

        when(passwordEncoder.matches(
                newPassword,
                encodedPassword
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateResetPassword(user, request)
        );

        verify(passwordEncoder)
                .matches(newPassword, encodedPassword);
    }
}