package lk.ashan.routenetlkserverapllication.module.privilege.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.privilege.mapper.PrivilegeMapper;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Privilege;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.ModuleRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.OperationRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.PrivilegeRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.validation.PrivilegeAssignmentContext;
import lk.ashan.routenetlkserverapllication.module.privilege.validation.PrivilegeValidator;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.user.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionRolledbackException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeValidator privilegeValidator;
    private final PrivilegeMapper privilegeMapper;

    @Transactional(readOnly = true)
    public List<PrivilegeResponseDto> getPrivileges() {
        return privilegeMapper.toDtoList(privilegeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PrivilegeResponseDto> searchPrivileges(
            @NotNull HashMap<String, String> params) {

        Specification<Privilege> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String roleId = params.get("ssrole");
            String moduleId = params.get("ssmodule");
            String operationId = params.get("ssoperation");

            if (roleId != null && !roleId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("role").get("id"),
                                Integer.parseInt(roleId)
                        )
                );
            }

            if (moduleId != null && !moduleId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("module").get("id"),
                                Integer.parseInt(moduleId)
                        )
                );
            }

            if (operationId != null && !operationId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("operation").get("id"),
                                Integer.parseInt(operationId)
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        List<Privilege> privileges =
                privilegeRepository.findAll(specification);

        return privilegeMapper.toDtoList(privileges);
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void assignPrivileges(Integer roleId, PrivilegeAssignRequestDto requestDto) {

        List<Privilege> privileges = new ArrayList<>();

        for (PrivilegeRequestDto privilegeDto : requestDto.getPrivileges()) {

            PrivilegeAssignmentContext context =
                    privilegeValidator.validateAssignment(
                            roleId,
                            privilegeDto
                    );

            Privilege privilege = new Privilege();

            privilege.setRole(context.role());
            privilege.setModule(context.module());
            privilege.setOperation(context.operation());

            privilege.setAuthority(
                    generateAuthority(
                            context.module(),
                            context.operation()
                    )
            );

            privileges.add(privilege);
        }

        privilegeRepository.saveAll(privileges);
    }

    private String generateAuthority(Module module, Operation operation) {

        String moduleName = module.getName()
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "-");

        String operationName = operation.getOperation()
                .trim()
                .toLowerCase();

        return moduleName + "-" + operationName;
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void removePrivilege(Integer roleId, Integer privilegeId) {

        // Validate role exists
        roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id : " + roleId + " not found"));

        // Validate privilege belongs to this role
        boolean privilegeExists = privilegeRepository.existsByRoleIdAndId(roleId, privilegeId);

        if (!privilegeExists) {
            throw new ResourceNotFoundException(
                    "Privilege with id : " + privilegeId + " not found for role id : " + roleId
            );
        }

        // Remove privilege mapping
        privilegeRepository.deleteByRoleIdAndId(roleId, privilegeId);
    }
}
