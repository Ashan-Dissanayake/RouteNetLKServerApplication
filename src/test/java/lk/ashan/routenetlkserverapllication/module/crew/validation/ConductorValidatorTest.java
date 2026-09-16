package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConductorValidatorTest {

    @Mock
    private ConductorRepository conductorRepository;

    @InjectMocks
    private ConductorValidator validator;


    // =========================
    // CREATE
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenCrewStatusIsNotEligible() {

        ConductorCreateRequestDto dto =
                mock(
                        ConductorCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Ineligible");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(conductorRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenRouteFamiliarityIsNotLow() {

        ConductorCreateRequestDto dto =
                mock(
                        ConductorCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("High");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(conductorRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenEmployeeAlreadyExists() {

        ConductorCreateRequestDto dto =
                mock(
                        ConductorCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(conductorRepository.existsByEmployeeId(100))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(dto)
        );

        verify(conductorRepository)
                .existsByEmployeeId(100);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenMedicalExpiryIsNotInFuture() {

        ConductorCreateRequestDto dto =
                mock(
                        ConductorCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now().minusMonths(1));

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().minusDays(1));

        when(conductorRepository.existsByEmployeeId(100))
                .thenReturn(false);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verify(conductorRepository)
                .existsByEmployeeId(100);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenMedicalValidityExceedsSixMonths() {

        ConductorCreateRequestDto dto =
                mock(
                        ConductorCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(7));

        when(conductorRepository.existsByEmployeeId(100))
                .thenReturn(false);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verify(conductorRepository)
                .existsByEmployeeId(100);
    }

    @Test
    void validateCreate_shouldPassWhenAllConditionsAreValid() {

        ConductorCreateRequestDto dto =
                mock(
                        ConductorCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(5));

        when(conductorRepository.existsByEmployeeId(100))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateCreate(dto)
        );

        verify(conductorRepository)
                .existsByEmployeeId(100);
    }


    // =========================
    // UPDATE
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenEmployeeAlreadyExistsForAnotherConductor() {

        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(
                        Employee.builder()
                                .id(100)
                                .build()
                )
                .build();

        ConductorUpdateRequestDto dto =
                mock(
                        ConductorUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getId())
                .thenReturn(1);

        when(dto.getEmployee().getId())
                .thenReturn(101);

        when(conductorRepository.existsByEmployeeIdAndIdNot(
                101,
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(
                        existingConductor,
                        dto
                )
        );

        verify(conductorRepository)
                .existsByEmployeeIdAndIdNot(101, 1);
    }

    @Test
    void validateUpdate_shouldPassWhenEmployeeIdIsUnchanged() {

        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(
                        Employee.builder()
                                .id(100)
                                .build()
                )
                .build();

        ConductorUpdateRequestDto dto =
                mock(
                        ConductorUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(5));

        assertDoesNotThrow(
                () -> validator.validateUpdate(
                        existingConductor,
                        dto
                )
        );

        verifyNoInteractions(conductorRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenEmployeeIdIsChangedAndNoDuplicateExists() {

        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(
                        Employee.builder()
                                .id(100)
                                .build()
                )
                .build();

        ConductorUpdateRequestDto dto =
                mock(
                        ConductorUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getEmployee().getId())
                .thenReturn(101);

        when(dto.getId())
                .thenReturn(1);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(5));

        when(conductorRepository.existsByEmployeeIdAndIdNot(
                101,
                1
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateUpdate(
                        existingConductor,
                        dto
                )
        );

        verify(conductorRepository)
                .existsByEmployeeIdAndIdNot(101, 1);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenMedicalExpiryIsNotInFuture() {

        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(
                        Employee.builder()
                                .id(100)
                                .build()
                )
                .build();

        ConductorUpdateRequestDto dto =
                mock(
                        ConductorUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now().minusMonths(1));

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().minusDays(1));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(
                        existingConductor,
                        dto
                )
        );

        verifyNoInteractions(conductorRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenMedicalValidityExceedsSixMonths() {

        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(
                        Employee.builder()
                                .id(100)
                                .build()
                )
                .build();

        ConductorUpdateRequestDto dto =
                mock(
                        ConductorUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(7));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(
                        existingConductor,
                        dto
                )
        );

        verifyNoInteractions(conductorRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenMedicalDetailsAreValid() {

        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(
                        Employee.builder()
                                .id(100)
                                .build()
                )
                .build();

        ConductorUpdateRequestDto dto =
                mock(
                        ConductorUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getEmployee().getId())
                .thenReturn(100);

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(5));

        assertDoesNotThrow(
                () -> validator.validateUpdate(
                        existingConductor,
                        dto
                )
        );

        verifyNoInteractions(conductorRepository);
    }
}