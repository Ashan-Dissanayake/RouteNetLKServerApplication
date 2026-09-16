package lk.ashan.routenetlkserverapllication.module.privilege.validation;

import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeRequestDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.ModuleRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.OperationRepository;
import lk.ashan.routenetlkserverapllication.module.privilege.repository.PrivilegeRepository;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import lk.ashan.routenetlkserverapllication.module.user.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrivilegeValidatorTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private PrivilegeValidator validator;

    // =========================================================
    // ASSIGNMENT - ROLE VALIDATION
    // =========================================================

    @Test
    void validateAssignment_shouldThrowExceptionWhenRoleDoesNotExist() {

        Integer roleId = 1;

        PrivilegeRequestDto request =
                mock(PrivilegeRequestDto.class, RETURNS_DEEP_STUBS);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> validator.validateAssignment(roleId, request)
        );

        verify(roleRepository).findById(roleId);
        verifyNoInteractions(
                moduleRepository,
                operationRepository,
                privilegeRepository
        );
    }

    // =========================================================
    // ASSIGNMENT - MODULE VALIDATION
    // =========================================================

    @Test
    void validateAssignment_shouldThrowExceptionWhenModuleDoesNotExist() {

        Integer roleId = 1;

        Role role = mock(Role.class);

        PrivilegeRequestDto request =
                mock(PrivilegeRequestDto.class, RETURNS_DEEP_STUBS);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(request.getModule().getId())
                .thenReturn(10);

        when(moduleRepository.findById(10))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> validator.validateAssignment(roleId, request)
        );

        verify(roleRepository).findById(roleId);
        verify(moduleRepository).findById(10);

        verifyNoInteractions(
                operationRepository,
                privilegeRepository
        );
    }

    // =========================================================
    // ASSIGNMENT - OPERATION VALIDATION
    // =========================================================

    @Test
    void validateAssignment_shouldThrowExceptionWhenOperationDoesNotExist() {

        Integer roleId = 1;

        Role role = mock(Role.class);
        Module module = mock(Module.class);

        PrivilegeRequestDto request =
                mock(PrivilegeRequestDto.class, RETURNS_DEEP_STUBS);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(request.getModule().getId())
                .thenReturn(10);

        when(moduleRepository.findById(10))
                .thenReturn(Optional.of(module));

        when(request.getOperation().getId())
                .thenReturn(100);

        when(operationRepository.findById(100))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> validator.validateAssignment(roleId, request)
        );

        verify(roleRepository).findById(roleId);
        verify(moduleRepository).findById(10);
        verify(operationRepository).findById(100);

        verifyNoInteractions(privilegeRepository);
    }

    // =========================================================
    // ASSIGNMENT - OPERATION / MODULE RELATIONSHIP
    // =========================================================

    @Test
    void validateAssignment_shouldThrowExceptionWhenOperationDoesNotBelongToModule() {

        Integer roleId = 1;

        Role role = mock(Role.class);
        Module module = mock(Module.class);
        Module operationModule = mock(Module.class);
        Operation operation = mock(Operation.class);

        PrivilegeRequestDto request =
                mock(PrivilegeRequestDto.class, RETURNS_DEEP_STUBS);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(request.getModule().getId())
                .thenReturn(10);

        when(moduleRepository.findById(10))
                .thenReturn(Optional.of(module));

        when(request.getOperation().getId())
                .thenReturn(100);

        when(operationRepository.findById(100))
                .thenReturn(Optional.of(operation));

        when(module.getId())
                .thenReturn(10);

        when(operation.getModule())
                .thenReturn(operationModule);

        when(operationModule.getId())
                .thenReturn(20);

        when(module.getName())
                .thenReturn("Fleet");

        when(operation.getDisplayname())
                .thenReturn("Create Vehicle");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateAssignment(roleId, request)
        );

        verify(roleRepository).findById(roleId);
        verify(moduleRepository).findById(10);
        verify(operationRepository).findById(100);

        verifyNoInteractions(privilegeRepository);
    }

    @Test
    void validateAssignment_shouldPassWhenOperationBelongsToSelectedModule() {

        Integer roleId = 1;

        Role role = mock(Role.class);
        Module module = mock(Module.class);
        Module operationModule = mock(Module.class);
        Operation operation = mock(Operation.class);

        PrivilegeRequestDto request =
                mock(PrivilegeRequestDto.class, RETURNS_DEEP_STUBS);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(request.getModule().getId())
                .thenReturn(10);

        when(moduleRepository.findById(10))
                .thenReturn(Optional.of(module));

        when(request.getOperation().getId())
                .thenReturn(100);

        when(operationRepository.findById(100))
                .thenReturn(Optional.of(operation));

        when(module.getId())
                .thenReturn(10);

        when(operation.getId())
                .thenReturn(100);

        when(operation.getModule())
                .thenReturn(operationModule);

        when(operationModule.getId())
                .thenReturn(10);

        when(privilegeRepository
                .existsByRoleIdAndModuleIdAndOperationId(
                        roleId,
                        10,
                        100
                ))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateAssignment(roleId, request)
        );
    }

    // =========================================================
    // ASSIGNMENT - DUPLICATE PRIVILEGE
    // =========================================================

    @Test
    void validateAssignment_shouldThrowExceptionWhenPrivilegeAlreadyExists() {

        Integer roleId = 1;

        Role role = mock(Role.class);
        Module module = mock(Module.class);
        Module operationModule = mock(Module.class);
        Operation operation = mock(Operation.class);

        PrivilegeRequestDto request =
                mock(PrivilegeRequestDto.class, RETURNS_DEEP_STUBS);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(request.getModule().getId())
                .thenReturn(10);

        when(moduleRepository.findById(10))
                .thenReturn(Optional.of(module));

        when(request.getOperation().getId())
                .thenReturn(100);

        when(operationRepository.findById(100))
                .thenReturn(Optional.of(operation));

        when(module.getId())
                .thenReturn(10);

        when(operation.getId())
                .thenReturn(100);

        when(operation.getModule())
                .thenReturn(operationModule);

        when(operationModule.getId())
                .thenReturn(10);

        when(privilegeRepository
                .existsByRoleIdAndModuleIdAndOperationId(
                        roleId,
                        10,
                        100
                ))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateAssignment(roleId, request)
        );

        verify(privilegeRepository)
                .existsByRoleIdAndModuleIdAndOperationId(
                        roleId,
                        10,
                        100
                );
    }

    // =========================================================
    // ASSIGNMENT - VALID
    // =========================================================

    @Test
    void validateAssignment_shouldReturnContextWhenAssignmentIsValid() {

        Integer roleId = 1;

        Role role = mock(Role.class);
        Module module = mock(Module.class);
        Module operationModule = mock(Module.class);
        Operation operation = mock(Operation.class);

        PrivilegeRequestDto request =
                mock(PrivilegeRequestDto.class, RETURNS_DEEP_STUBS);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(request.getModule().getId())
                .thenReturn(10);

        when(moduleRepository.findById(10))
                .thenReturn(Optional.of(module));

        when(request.getOperation().getId())
                .thenReturn(100);

        when(operationRepository.findById(100))
                .thenReturn(Optional.of(operation));

        when(module.getId())
                .thenReturn(10);

        when(operation.getId())
                .thenReturn(100);

        when(operation.getModule())
                .thenReturn(operationModule);

        when(operationModule.getId())
                .thenReturn(10);

        when(privilegeRepository
                .existsByRoleIdAndModuleIdAndOperationId(
                        roleId,
                        10,
                        100
                ))
                .thenReturn(false);

        PrivilegeAssignmentContext result =
                validator.validateAssignment(roleId, request);

        assertNotNull(result);
        assertSame(role, result.role());
        assertSame(module, result.module());
        assertSame(operation, result.operation());
    }

    // =========================================================
    // REMOVAL - ROLE VALIDATION
    // =========================================================

    @Test
    void validateRemoval_shouldThrowExceptionWhenRoleDoesNotExist() {

        Integer roleId = 1;
        Integer privilegeId = 100;

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> validator.validateRemoval(
                        roleId,
                        privilegeId
                )
        );

        verify(roleRepository).findById(roleId);

        verifyNoInteractions(privilegeRepository);
    }

    // =========================================================
    // REMOVAL - PRIVILEGE VALIDATION
    // =========================================================

    @Test
    void validateRemoval_shouldThrowExceptionWhenPrivilegeDoesNotBelongToRole() {

        Integer roleId = 1;
        Integer privilegeId = 100;

        Role role = mock(Role.class);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(privilegeRepository
                .existsByRoleIdAndId(roleId, privilegeId))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> validator.validateRemoval(
                        roleId,
                        privilegeId
                )
        );

        verify(roleRepository).findById(roleId);

        verify(privilegeRepository)
                .existsByRoleIdAndId(
                        roleId,
                        privilegeId
                );
    }

    // =========================================================
    // REMOVAL - VALID
    // =========================================================

    @Test
    void validateRemoval_shouldPassWhenPrivilegeBelongsToRole() {

        Integer roleId = 1;
        Integer privilegeId = 100;

        Role role = mock(Role.class);

        when(roleRepository.findById(roleId))
                .thenReturn(Optional.of(role));

        when(privilegeRepository
                .existsByRoleIdAndId(roleId, privilegeId))
                .thenReturn(true);

        assertDoesNotThrow(
                () -> validator.validateRemoval(
                        roleId,
                        privilegeId
                )
        );

        verify(roleRepository).findById(roleId);

        verify(privilegeRepository)
                .existsByRoleIdAndId(
                        roleId,
                        privilegeId
                );
    }
}