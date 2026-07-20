package lk.ashan.routenetlkserverapllication.module.privilege.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.privilege.mapper.PrivilegeMapper;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Privilege;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.ModuleRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.OperationRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.PrivilegeRepository;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.user.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionRolledbackException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final OperationRepository operationRepository;
    private final PrivilegeMapper privilegeMapper;

    @Transactional(readOnly = true)
    public List<PrivilegeResponseDto> getPrivileges() {
        return privilegeMapper.toDtoList(privilegeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PrivilegeResponseDto> searchPrivileges(@NotNull HashMap<String, String> params) {

        List<Privilege> privileges = privilegeRepository.findAll();

        String roleId = params.get("ssrole");
        String moduleId= params.get("ssmodule");
        String operationId= params.get("ssoperation");

        Stream<Privilege> privilegeStream = privileges.stream();

        if(roleId!=null)privilegeStream = privilegeStream.filter(r->r.getRole().getId()==Integer.parseInt(roleId));
        if(moduleId!=null)privilegeStream = privilegeStream.filter(r->r.getModule().getId()==Integer.parseInt(moduleId));
        if(operationId!=null)privilegeStream = privilegeStream.filter(r->r.getOperation().getId()==Integer.parseInt(operationId));

        return privilegeMapper.toDtoList( privilegeStream.collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = TransactionRolledbackException.class)
    public void assignPrivileges(Integer roleId, PrivilegeAssignRequestDto requestDto) {
        // Validate role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id : " + roleId + " not found"));

        List<Privilege> privileges = new ArrayList<>();

        for (PrivilegeRequestDto privilegeDto : requestDto.getPrivileges()) {
            // Validate module
            Module module = moduleRepository.findById(privilegeDto.getModule().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Module with id : " + privilegeDto.getModule().getId() + " not found")
                    );

            // Validate operation
            Operation operation = operationRepository.findById(privilegeDto.getOperation().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                                    "Operation with id : " + privilegeDto.getOperation().getId() + " not found"
                    ));

            // Validate operation belongs to selected module
            if (!operation.getModule().getId().equals(module.getId())) {
                throw new BusinessRuleViolationException(
                        "Operation does not belong to the selected module-\t"
                                + module.getName() + "-" + operation.getDisplayname()
                );
            }

            // Prevent duplicate privilege
            if (privilegeRepository.existsByRoleIdAndModuleIdAndOperationId(
                    roleId, module.getId(), operation.getId()
            )) {
                throw new ResourceExistsException("Privilege already assigned to the role");
            }

            String authority = generateAuthority(module, operation);

            Privilege privilege = new Privilege();
            privilege.setRole(role);
            privilege.setModule(module);
            privilege.setOperation(operation);
            privilege.setAuthority(authority);

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

    @Transactional(readOnly = true)
    public RolePrivilegeResponseDto getRolePrivilege(Integer roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role with id : " + roleId + " not found"
                        )
                );


        List<Privilege> privileges = privilegeRepository.findByRoleId(roleId);


        List<PrivilegeResponseDto> privilegeResponseList =
                privileges.stream()
                        .map(privilege -> PrivilegeResponseDto.builder()
                                .id(privilege.getId())
                                .authority(privilege.getAuthority())
                                .module(
                                        ModuleDto.builder()
                                                .id(privilege.getModule().getId())
                                                .name(privilege.getModule().getName())
                                                .build()
                                )
                                .operation(
                                        OperationDto.builder()
                                                .id(privilege.getOperation().getId())
                                                .displayname(privilege.getOperation().getDisplayname())
                                                .operation(privilege.getOperation().getOperation())
                                                .build()
                                )
                                .build()
                        )
                        .toList();

        return RolePrivilegeResponseDto.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .privileges(privilegeResponseList)
                .build();
    }
}
