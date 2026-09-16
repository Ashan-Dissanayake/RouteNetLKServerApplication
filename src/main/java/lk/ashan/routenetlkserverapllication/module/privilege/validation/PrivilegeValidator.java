package lk.ashan.routenetlkserverapllication.module.privilege.validation;

import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeRequestDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.ModuleRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.OperationRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.PrivilegeRepository;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.user.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PrivilegeValidator {

    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final OperationRepository operationRepository;
    private final PrivilegeRepository privilegeRepository;

    public PrivilegeAssignmentContext validateAssignment(Integer roleId, PrivilegeRequestDto request) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role with id : " + roleId + " not found"
                        )
                );

        Module module = moduleRepository.findById(
                request.getModule().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Module with id : "
                                + request.getModule().getId()
                                + " not found"
                )
        );

        Operation operation = operationRepository.findById(
                request.getOperation().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Operation with id : "
                                + request.getOperation().getId()
                                + " not found"
                )
        );

        validateOperationBelongsToModule(module, operation);

        validateDuplicatePrivilege(roleId, module.getId(), operation.getId());

        return new PrivilegeAssignmentContext(role, module, operation);
    }

    public void validateRemoval(Integer roleId, Integer privilegeId) {

        roleRepository.findById(roleId).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role with id : " + roleId + " not found"
                        )
                );

        boolean exists = privilegeRepository.existsByRoleIdAndId(roleId, privilegeId);

        if (!exists) {
            throw new ResourceNotFoundException(
                    "Privilege with id : "
                            + privilegeId
                            + " not found for role id : "
                            + roleId
            );
        }
    }

    private void validateOperationBelongsToModule(Module module, Operation operation) {

        if (!operation.getModule().getId().equals(module.getId())) {

            throw new BusinessRuleViolationException(
                    "Operation does not belong to the selected module-"
                            + module.getName()
                            + "-"
                            + operation.getDisplayname()
            );
        }
    }

    private void validateDuplicatePrivilege(Integer roleId, Integer moduleId, Integer operationId) {
        if (privilegeRepository.existsByRoleIdAndModuleIdAndOperationId(roleId, moduleId, operationId)) {
            throw new ResourceExistsException(
                    "Privilege already assigned to the role"
            );
        }
    }
}