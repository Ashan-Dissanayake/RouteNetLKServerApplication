package lk.ashan.routenetlkserverapllication.module.user.service;

import lk.ashan.routenetlkserverapllication.module.user.mapper.UserRoleMapper;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserRoleAssignRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserRoleResponseDto;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserRole;
import lk.ashan.routenetlkserverapllication.module.user.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRoleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionRolledbackException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final UserRoleMapper userRoleMapper;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserRoleResponseDto> getUserRoles() {
        return userRoleMapper.toDtoList(userRoleRepository.findAll());
    }


    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void assignRoles(
            Integer userId,
            UserRoleAssignRequestDto requestDto
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id : " + userId + " not found"
                        )
                );


        List<UserRole> userRoles = new ArrayList<>();


        for (RoleDto roleDto : requestDto.getRoles()) {


            Role role = roleRepository.findById(roleDto.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Role with id : "
                                            + roleDto.getId()
                                            + " not found"
                            )
                    );


            if (userRoleRepository.existsByUserIdAndRoleId(
                    userId,
                    roleDto.getId()
            )) {

                throw new ResourceExistsException(
                        "User already has role : "
                                + role.getName()
                );
            }

            UserRole userRole = new UserRole();

            userRole.setUser(user);
            userRole.setRole(role);

            userRoles.add(userRole);
        }

        userRoleRepository.saveAll(userRoles);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void removeRole(Integer userId, Integer roleId) {
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id : " + userId + " not found"
                        )
                );

        roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role with id : " + roleId + " not found"
                        )
                );

        if (!userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new ResourceNotFoundException(
                    "Role is not assigned to the user"
            );
        }

        userRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void replaceRoles(Integer userId, UserRoleAssignRequestDto requestDto) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "User with id : "
                                            + userId
                                            + " not found"
                            )
                    );


            List<UserRole> newUserRoles = new ArrayList<>();


            for (RoleDto roleDto : requestDto.getRoles()) {

                Role role = roleRepository.findById(roleDto.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Role with id : "
                                                + roleDto.getId()
                                                + " not found"
                                )
                        );


                UserRole userRole = new UserRole();

                userRole.setUser(user);
                userRole.setRole(role);

                newUserRoles.add(userRole);
            }

            // Remove existing roles
            userRoleRepository.deleteByUserId(userId);

            // Assign new roles
            userRoleRepository.saveAll(newUserRoles);
    }
}
