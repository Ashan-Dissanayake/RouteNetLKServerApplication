package lk.ashan.routenetlkserverapllication.module.user.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.user.mapper.UserMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserStatus;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserStatusRepository;
import lk.ashan.routenetlkserverapllication.module.user.validation.UserValidator;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionRolledbackException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserStatusService userStatusService;
    private final UserValidator userValidator;
    private final UserStatusRepository userStatusRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public List<UserDetailResponseDto> getUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<UserDetailResponseDto> searchUsers(
            @NotNull HashMap<String, String> params) {

        Specification<User> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String employeeId = params.get("ssemployee");
            String username = params.get("ssuseranme");
            String userTypeId = params.get("ssusertype");

            if (employeeId != null && !employeeId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("employee").get("id"),
                                Integer.parseInt(employeeId)
                        )
                );
            }

            if (username != null && !username.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("username"),
                                username
                        )
                );
            }

            if (userTypeId != null && !userTypeId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("usertype").get("id"),
                                Integer.parseInt(userTypeId)
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        List<User> users = userRepository.findAll(specification);

        return userMapper.toDtoList(users);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public UserDetailResponseDto createUser(UserCreateRequestDto request) {

        userValidator.validateCreate(request);

        User user = userMapper.toEntity(request);

        Optional.ofNullable(user.getUserRoles())
                .ifPresent(userRoles ->
                        userRoles.forEach(role ->
                                role.setUser(user)
                        )
                );

        UserStatus defaultStatus =
                userStatusService.getByName("Active");

        user.setUserstatus(defaultStatus);
        user.setAccountlocked(false);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public UserDetailResponseDto updateUser(UserUpdateRequestDto request) {

        User existingUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userValidator.validateUpdate(existingUser, request);

        User updatedUser = userMapper.toEntity(request);

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
    public void activateOrDeactivateUser(UserActiveDeactiveDto request) {

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException(
                                "User with username : "
                                + request.getUsername()
                                + " not found"
                )
        );

        userValidator.validateActivation(user, request.getAccountLocked());

        boolean lockAccount = Boolean.TRUE.equals(request.getAccountLocked());

        UserStatus status = userStatusRepository.findByName(lockAccount ? "Locked" : "Active")
                .orElseThrow(() -> new ResourceNotFoundException("User status not found"));

        user.setUserstatus(status);
        user.setAccountlocked(lockAccount);

        userRepository.save(user);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void changePassword(Integer userId, ChangePasswordRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id : " + userId + " not found"));

        userValidator.validateChangePassword(user, request);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void resetPassword(Integer userId, ResetPasswordRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id : " + userId + " not found"
                        )
                );

        userValidator.validateResetPassword(user, request);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

}
