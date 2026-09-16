package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverValidatorTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverValidator validator;


    // =========================
    // CREATE - BUSINESS RULES
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenCrewStatusIsNotEligible() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Ineligible");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenRouteFamiliarityIsNotLow() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
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

        verifyNoInteractions(driverRepository);
    }


    // =========================
    // CREATE - LICENSE
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenLicenseIssuedDateIsInFuture() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now().plusDays(1));

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(1));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenLicenseExpiryIsBeforeIssuedDate() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().minusDays(1));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenLicenseValidityExceedsFourYears() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(5));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(driverRepository);
    }


    // =========================
    // CREATE - MEDICAL
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenMedicalIssuedDateIsInFuture() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(1));

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now().plusDays(1));

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(6));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenMedicalValidityExceedsSixMonths() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(1));

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(7));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(dto)
        );

        verifyNoInteractions(driverRepository);
    }


    // =========================
    // CREATE - UNIQUENESS
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenLicenseNumberExists() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(1));

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(6));

        when(dto.getLicensenumber())
                .thenReturn("LN123");

        when(driverRepository.existsByLicensenumber("LN123"))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(dto)
        );

        verify(driverRepository)
                .existsByLicensenumber("LN123");
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenEmployeeAlreadyHasDriverProfile() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(1));

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(6));

        when(dto.getLicensenumber())
                .thenReturn("LN123");

        when(dto.getEmployee().getId())
                .thenReturn(1);

        when(driverRepository.existsByLicensenumber("LN123"))
                .thenReturn(false);

        when(driverRepository.existsByEmployeeId(1))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(dto)
        );

        verify(driverRepository)
                .existsByLicensenumber("LN123");

        verify(driverRepository)
                .existsByEmployeeId(1);
    }

    @Test
    void validateCreate_shouldPassWhenAllConditionsAreValid() {

        DriverCreateRequestDto dto =
                mock(
                        DriverCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getCrewstatus().getName())
                .thenReturn("Eligible");

        when(dto.getRoutefamiliaritylevel().getName())
                .thenReturn("Low");

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(1));

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(6));

        when(dto.getLicensenumber())
                .thenReturn("LN123");

        when(dto.getEmployee().getId())
                .thenReturn(1);

        when(driverRepository.existsByLicensenumber("LN123"))
                .thenReturn(false);

        when(driverRepository.existsByEmployeeId(1))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateCreate(dto)
        );

        verify(driverRepository)
                .existsByLicensenumber("LN123");

        verify(driverRepository)
                .existsByEmployeeId(1);
    }


    // =========================
    // UPDATE - IMMUTABLE FIELDS
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenLicenseNumberIsModified() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                mock(
                        DriverUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getLicensenumber())
                .thenReturn("XYZ789");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenEmployeeIsReassigned() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                mock(
                        DriverUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getLicensenumber())
                .thenReturn("ABC123");

        when(dto.getEmployee().getId())
                .thenReturn(2);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenLicenseIssuedDateIsModified() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                mock(
                        DriverUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getLicensenumber())
                .thenReturn("ABC123");

        when(dto.getEmployee().getId())
                .thenReturn(1);

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.of(2023, 2, 1));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }


    // =========================
    // UPDATE - LICENSE
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenLicenseIssuedDateIsInFuture() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                mock(
                        DriverUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getLicensenumber())
                .thenReturn("ABC123");

        when(dto.getEmployee().getId())
                .thenReturn(1);

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now().plusDays(1));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenLicenseExpiryIsBeforeIssuedDate() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                createValidUpdateDto();

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().minusDays(1));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenLicenseValidityExceedsFourYears() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                createValidUpdateDto();

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(5));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }


    // =========================
    // UPDATE - MEDICAL
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenMedicalIssuedDateIsInFuture() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                createValidUpdateDto();

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now().plusDays(1));

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(6));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenMedicalValidityExceedsSixMonths() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                createValidUpdateDto();

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(7));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verifyNoInteractions(driverRepository);
    }


    // =========================
    // UPDATE - UNIQUENESS
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenLicenseNumberExistsForAnotherDriver() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                createValidUpdateDto();

        when(dto.getId())
                .thenReturn(1);

        when(driverRepository.existsByLicensenumberAndIdNot(
                "ABC123",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verify(driverRepository)
                .existsByLicensenumberAndIdNot("ABC123", 1);

        verifyNoMoreInteractions(driverRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenEmployeeExistsForAnotherDriver() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                createValidUpdateDto();

        when(dto.getId())
                .thenReturn(1);

        when(driverRepository.existsByLicensenumberAndIdNot(
                "ABC123",
                1
        )).thenReturn(false);

        when(driverRepository.existsByEmployeeIdAndIdNot(
                1,
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(existing, dto)
        );

        verify(driverRepository)
                .existsByLicensenumberAndIdNot("ABC123", 1);

        verify(driverRepository)
                .existsByEmployeeIdAndIdNot(1, 1);

        verifyNoMoreInteractions(driverRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenAllFieldsAreValidAndUnique() {

        Driver existing = createValidExistingDriver();

        DriverUpdateRequestDto dto =
                createValidUpdateDto();

        when(dto.getId())
                .thenReturn(1);

        when(driverRepository.existsByLicensenumberAndIdNot(
                "ABC123",
                1
        )).thenReturn(false);

        when(driverRepository.existsByEmployeeIdAndIdNot(
                1,
                1
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateUpdate(existing, dto)
        );

        verify(driverRepository)
                .existsByLicensenumberAndIdNot("ABC123", 1);

        verify(driverRepository)
                .existsByEmployeeIdAndIdNot(1, 1);

        verifyNoMoreInteractions(driverRepository);
    }


    // =========================
    // HELPERS
    // =========================

    private Driver createValidExistingDriver() {

        return Driver.builder()
                .id(1)
                .licensenumber("ABC123")
                .employee(
                        Employee.builder()
                                .id(1)
                                .build()
                )
                .dolicenseissued(LocalDate.now())
                .build();
    }

    private DriverUpdateRequestDto createValidUpdateDto() {

        DriverUpdateRequestDto dto =
                mock(
                        DriverUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(dto.getLicensenumber())
                .thenReturn("ABC123");

        when(dto.getEmployee().getId())
                .thenReturn(1);

        when(dto.getDolicenseissued())
                .thenReturn(LocalDate.now());

        when(dto.getDolicenseexpired())
                .thenReturn(LocalDate.now().plusYears(1));

        when(dto.getDomedicalissued())
                .thenReturn(LocalDate.now());

        when(dto.getDomedicalexpired())
                .thenReturn(LocalDate.now().plusMonths(6));

        return dto;
    }
}