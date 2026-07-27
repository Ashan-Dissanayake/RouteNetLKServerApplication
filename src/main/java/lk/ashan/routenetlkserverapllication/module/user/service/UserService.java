package lk.ashan.routenetlkserverapllication.module.user.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeeService;
import lk.ashan.routenetlkserverapllication.module.user.mapper.UserMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserStatus;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionRolledbackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserStatusService userStatusService;
    private final UserStatusRepository userStatusRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public List<UserDetailResponseDto> getUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<UserDetailResponseDto> searchUsers(@NotNull HashMap<String, String> params) {

        String employeeId = params.get("ssemployee");
        String username = params.get("ssuseranme");
        String userTypeId = params.get("ssusertype");

        Stream<User> userStream = userRepository.findAll().stream();

        if (employeeId != null)
            userStream = userStream.filter(u -> u.getEmployee().getId() == Integer.parseInt(employeeId));
        if (username != null)
            userStream = userStream.filter(u -> u.getUsername().equals(username));
        if (userTypeId != null)
            userStream = userStream.filter(u -> u.getUsertype().getId() == Integer.parseInt(userTypeId));

        return userMapper.toDtoList(userStream.collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public UserDetailResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {

        if (userRepository.existsByUsername(userCreateRequestDto.getUsername())) {
            throw new ResourceExistsException(
                    "User with name : " + userCreateRequestDto.getUsername() + " already exists"
            );
        }

        // Validate employee already has a user account
        if (userRepository.existsByEmployee_Id(userCreateRequestDto.getEmployee().getId())) {
            throw new ResourceExistsException(
                    "Employee already has a user account"
            );
        }

        User userEntity = userMapper.toEntity(userCreateRequestDto);

        // Map bidirectional relationship User -> UserRole
        Optional.ofNullable(userEntity.getUserRoles())
                .ifPresent(userRoles -> userRoles.forEach(userRole ->
                        userRole.setUser(userEntity)
                ));

        // Always assign default user status during creation
        UserStatus defaultStatus = userStatusService.getByName("Active");

        userEntity.setUserstatus(defaultStatus);

        // Default account lock status
        userEntity.setAccountlocked(false);

        // Encrypt password
        userEntity.setPassword(
                passwordEncoder.encode(userCreateRequestDto.getPassword())
        );

        User savedUser =  userRepository.save(userEntity);
        return userMapper.toDto(savedUser);

    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public UserDetailResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto) {

        User existingUser = userRepository.findById(
                userUpdateRequestDto.getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("User not found")
        );

        String employeeStatus = existingUser.getEmployee()
                .getEmployeestatus()
                .getName();


        if ("RESIGNED".equalsIgnoreCase(employeeStatus)) {
            throw new ResourceExistsException(
                    "Cannot update user. Employee has resigned"
            );
        }


        if (!existingUser.getUsername()
                .equals(userUpdateRequestDto.getUsername())
                &&
                userRepository.existsByUsernameAndIdNot(
                        userUpdateRequestDto.getUsername(),
                        existingUser.getId()
                )) {

            throw new ResourceExistsException(
                    "Username already exists"
            );
        }


        User updatedUser = userMapper.toEntity(userUpdateRequestDto);


        BeanUtils.copyProperties(
                updatedUser,
                existingUser,
                "id",
                "password",
                "accountLocked",
                "recoverycode",
                "recoverycodeexpiration",
                "recoverycodeused",
                "userroles",
                "userstatus"
        );

        return userMapper.toDto(userRepository.save(existingUser));
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void activateOrDeactivateUser(UserActiveDeactiveDto userActiveDeactiveDTO) {

        User user = userRepository.findByUsername(
                userActiveDeactiveDTO.getUsername()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "User with username : "
                                + userActiveDeactiveDTO.getUsername()
                                + " not found"
                )
        );

        // Employee lifecycle validation
        String employeeStatus = user.getEmployee()
                .getEmployeestatus()
                .getName();


        if ("RESIGNED".equalsIgnoreCase(employeeStatus)
                && Boolean.FALSE.equals(userActiveDeactiveDTO.getAccountLocked())) {

            throw new ResourceExistsException(
                    "Cannot activate user. Employee has resigned"
            );
        }


        boolean lockAccount = Boolean.TRUE.equals(
                userActiveDeactiveDTO.getAccountLocked()
        );


        UserStatus status;

        if (lockAccount) {
            status = userStatusRepository.findByName("Locked")
                    .orElseThrow(() -> new ResourceNotFoundException("Locked status not found"));
        } else {
            status = userStatusRepository.findByName("Active")
                    .orElseThrow(() -> new ResourceNotFoundException("Active status not found"));
        }

        user.setUserstatus(status);
        user.setAccountlocked(lockAccount);

        userRepository.save(user);


    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void changePassword(Integer userId, ChangePasswordRequestDto changePasswordRequestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id : " + userId + " not found"
                        )
                );

        // Validate current password
        if (!passwordEncoder.matches(
                changePasswordRequestDto.getCurrentPassword(),
                user.getPassword()
        )) {

            throw new ResourceExistsException(
                    "Current password is incorrect"
            );
        }


        // Prevent same password
        if (passwordEncoder.matches(
                changePasswordRequestDto.getNewPassword(),
                user.getPassword()
        )) {

            throw new ResourceExistsException(
                    "New password cannot be same as current password"
            );
        }


        user.setPassword(
                passwordEncoder.encode(
                        changePasswordRequestDto.getNewPassword()
                )
        );

        userRepository.save(user);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void resetPassword(Integer userId, ResetPasswordRequestDto resetPasswordRequestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id : " + userId + " not found"
                        )
                );

        // Prevent setting same password again
        if (passwordEncoder.matches(
                resetPasswordRequestDto.getNewPassword(),
                user.getPassword()
        )) {

            throw new ResourceExistsException(
                    "New password cannot be same as current password"
            );
        }

        // Encode new password
        user.setPassword(
                passwordEncoder.encode(
                        resetPasswordRequestDto.getNewPassword()
                )
        );

        userRepository.save(user);
    }


}
