package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
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
class EmployeeValidatorTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeValidator validator;


    // =========================
    // CREATE - DESIGNATION RULES
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenInvalidDepartmentDesignationCombination() {

        EmployeeCreateRequestDto request =
                mock(
                        EmployeeCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("invalid department");

        when(request.getDesignation().getName())
                .thenReturn("driver");

        when(request.getGender().getName())
                .thenReturn("male");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenFemaleEmployeeIsDriver() {

        EmployeeCreateRequestDto request =
                mock(
                        EmployeeCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("operations");

        when(request.getDesignation().getName())
                .thenReturn("driver");

        when(request.getGender().getName())
                .thenReturn("female");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldPassWhenValidDepartmentDesignationCombination() {

        EmployeeCreateRequestDto request =
                createCreateRequestForFullValidation();

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );

        verify(employeeRepository)
                .existsByNic("199012345678");

        verify(employeeRepository)
                .existsByMobile("0771234567");

        verify(employeeRepository)
                .existsByEmergencycontact("0777654321");

        verify(employeeRepository)
                .existsByEmergencycontact("0771234567");

        verify(employeeRepository)
                .existsByMobile("0777654321");
    }


    // =========================
    // CREATE - EMPLOYMENT DATE
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenProbationerHasOldDateOfJoining() {

        EmployeeCreateRequestDto request =
                createCreateRequestForEmploymentValidation();

        when(request.getEmployeetype().getName())
                .thenReturn("probationers");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear() - 1,
                                1,
                                1
                        )
                );

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenContractEmployeeHasOldDateOfJoining() {

        EmployeeCreateRequestDto request =
                createCreateRequestForEmploymentValidation();

        when(request.getEmployeetype().getName())
                .thenReturn("contract");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear() - 1,
                                1,
                                1
                        )
                );

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldPassWhenProbationerHasCurrentYearDateOfJoining() {

        EmployeeCreateRequestDto request =
                createCreateRequestForEmploymentValidation();

        when(request.getEmployeetype().getName())
                .thenReturn("probationers");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear(),
                                1,
                                1
                        )
                );

        when(request.getGender().getName())
                .thenReturn("male");

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        stubUniqueEmployeeFields();

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldPassWhenPermanentEmployeeHasOldDateOfJoining() {

        EmployeeCreateRequestDto request =
                createCreateRequestForEmploymentValidation();

        when(request.getEmployeetype().getName())
                .thenReturn("permanent");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear() - 5,
                                1,
                                1
                        )
                );

        when(request.getGender().getName())
                .thenReturn("male");

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        stubUniqueEmployeeFields();

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }


    // =========================
    // CREATE - GENDER / NIC
    // =========================

    @Test
    void validateCreate_shouldPassWhenGenderMatchesNic() {

        EmployeeCreateRequestDto request =
                createCreateRequestForGenderNicValidation();

        when(request.getGender().getName())
                .thenReturn("Male");

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        stubUniqueEmployeeFields();

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenGenderDoesNotMatchNic() {

        EmployeeCreateRequestDto request =
                createCreateRequestForGenderNicValidation();

        when(request.getGender().getName())
                .thenReturn("Female");

        when(request.getNic())
                .thenReturn("199012345678");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenNicIsNull() {

        EmployeeCreateRequestDto request =
                createCreateRequestForGenderNicValidation();

        when(request.getGender().getName())
                .thenReturn("Male");

        when(request.getNic())
                .thenReturn(null);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenNicIsInvalid() {

        EmployeeCreateRequestDto request =
                createCreateRequestForGenderNicValidation();

        when(request.getGender().getName())
                .thenReturn("Male");

        when(request.getNic())
                .thenReturn("INVALID_NIC");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }


    // =========================
    // CREATE - UNIQUENESS
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenNicAlreadyExists() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        when(employeeRepository.existsByNic("123456789V"))
                .thenReturn(true);

        when(request.getNic())
                .thenReturn("123456789V");

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(employeeRepository)
                .existsByNic("123456789V");

        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenMobileAlreadyExists() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        when(request.getNic())
                .thenReturn("123456789V");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(employeeRepository.existsByNic("123456789V"))
                .thenReturn(false);

        when(employeeRepository.existsByMobile("0771234567"))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(employeeRepository)
                .existsByNic("123456789V");

        verify(employeeRepository)
                .existsByMobile("0771234567");
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenEmergencyContactAlreadyExists() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        when(request.getNic())
                .thenReturn("123456789V");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        when(employeeRepository.existsByNic("123456789V"))
                .thenReturn(false);

        when(employeeRepository.existsByMobile("0771234567"))
                .thenReturn(false);

        when(employeeRepository.existsByEmergencycontact("0777654321"))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(employeeRepository)
                .existsByNic("123456789V");

        verify(employeeRepository)
                .existsByMobile("0771234567");

        verify(employeeRepository)
                .existsByEmergencycontact("0777654321");
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenMobileUsedAsEmergencyContact() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        when(request.getNic())
                .thenReturn("123456789V");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        when(employeeRepository.existsByNic("123456789V"))
                .thenReturn(false);

        when(employeeRepository.existsByMobile("0771234567"))
                .thenReturn(false);

        when(employeeRepository.existsByEmergencycontact("0777654321"))
                .thenReturn(false);

        when(employeeRepository.existsByEmergencycontact("0771234567"))
                .thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenEmergencyContactUsedAsMobile() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        when(request.getNic())
                .thenReturn("123456789V");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        when(employeeRepository.existsByNic("123456789V"))
                .thenReturn(false);

        when(employeeRepository.existsByMobile("0771234567"))
                .thenReturn(false);

        when(employeeRepository.existsByEmergencycontact("0777654321"))
                .thenReturn(false);

        when(employeeRepository.existsByEmergencycontact("0771234567"))
                .thenReturn(false);

        when(employeeRepository.existsByMobile("0777654321"))
                .thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenMobileAndEmergencyContactAreSame() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0771234567");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateCreate_shouldPassWhenAllFieldsAreUnique() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        stubUniqueEmployeeFields();

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );

        verify(employeeRepository)
                .existsByNic("199012345678");

        verify(employeeRepository)
                .existsByMobile("0771234567");

        verify(employeeRepository)
                .existsByEmergencycontact("0777654321");

        verify(employeeRepository)
                .existsByEmergencycontact("0771234567");

        verify(employeeRepository)
                .existsByMobile("0777654321");
    }


    // =========================
    // UPDATE - DESIGNATION RULES
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenInvalidDepartmentDesignationCombination() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForDesignationValidation();

        when(request.getDepartment().getName())
                .thenReturn("invalid department");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenFemaleEmployeeIsDriver() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForDesignationValidation();

        when(request.getGender().getName())
                .thenReturn("female");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenValidDepartmentDesignationCombination() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForFullValidation();

        when(request.getDepartment().getName())
                .thenReturn("engineering and technical");

        when(request.getDesignation().getName())
                .thenReturn("mechanic");

        assertDoesNotThrow(
                () -> validator.validateUpdate(request)
        );

        verify(employeeRepository)
                .existsByNicAndIdNot("199012345678", 1);
    }


    // =========================
    // UPDATE - EMPLOYMENT DATE
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenProbationerHasOldDateOfJoining() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForEmploymentValidation();

        when(request.getEmployeetype().getName())
                .thenReturn("probationers");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear() - 1,
                                1,
                                1
                        )
                );

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenProbationerHasCurrentYearDateOfJoining() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForEmploymentValidation();

        when(request.getEmployeetype().getName())
                .thenReturn("probationers");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear(),
                                1,
                                1
                        )
                );

        when(request.getGender().getName())
                .thenReturn("male");

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        when(request.getId())
                .thenReturn(1);

        stubUniqueUpdateFields();

        assertDoesNotThrow(
                () -> validator.validateUpdate(request)
        );
    }


    // =========================
    // UPDATE - GENDER / NIC
    // =========================

    @Test
    void validateUpdate_shouldPassWhenGenderMatchesNic() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForGenderNicValidation();

        when(request.getGender().getName())
                .thenReturn("Male");

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        when(request.getId())
                .thenReturn(1);

        stubUniqueUpdateFields();

        assertDoesNotThrow(
                () -> validator.validateUpdate(request)
        );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenGenderDoesNotMatchNic() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForGenderNicValidation();

        when(request.getGender().getName())
                .thenReturn("Female");

        when(request.getNic())
                .thenReturn("199012345678");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request)
        );

        verifyNoInteractions(employeeRepository);
    }


    // =========================
    // UPDATE - UNIQUENESS
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenNicAlreadyExists() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForUniquenessValidation();

        when(employeeRepository.existsByNicAndIdNot(
                "199012345678",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(request)
        );

        verify(employeeRepository)
                .existsByNicAndIdNot("199012345678", 1);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenMobileAlreadyExists() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForUniquenessValidation();

        when(employeeRepository.existsByNicAndIdNot(
                "199012345678",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByMobileAndIdNot(
                "0771234567",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(request)
        );

        verify(employeeRepository)
                .existsByNicAndIdNot("199012345678", 1);

        verify(employeeRepository)
                .existsByMobileAndIdNot("0771234567", 1);
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenEmergencyContactAlreadyExists() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForUniquenessValidation();

        when(employeeRepository.existsByNicAndIdNot(
                "199012345678",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByMobileAndIdNot(
                "0771234567",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByEmergencycontactAndIdNot(
                "0777654321",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(request)
        );

        verify(employeeRepository)
                .existsByNicAndIdNot("199012345678", 1);

        verify(employeeRepository)
                .existsByMobileAndIdNot("0771234567", 1);

        verify(employeeRepository)
                .existsByEmergencycontactAndIdNot(
                        "0777654321",
                        1
                );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenMobileUsedAsEmergencyContact() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForUniquenessValidation();

        when(employeeRepository.existsByNicAndIdNot(
                "199012345678",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByMobileAndIdNot(
                "0771234567",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByEmergencycontactAndIdNot(
                "0777654321",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByEmergencycontactAndIdNot(
                "0771234567",
                1
        )).thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request)
        );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenEmergencyContactUsedAsMobile() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForUniquenessValidation();

        when(employeeRepository.existsByNicAndIdNot(
                "199012345678",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByMobileAndIdNot(
                "0771234567",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByEmergencycontactAndIdNot(
                "0777654321",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByEmergencycontactAndIdNot(
                "0771234567",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByMobileAndIdNot(
                "0777654321",
                1
        )).thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request)
        );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenMobileAndEmergencyContactAreSame() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForUniquenessValidation();

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0771234567");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateUpdate(request)
        );

        verifyNoInteractions(employeeRepository);
    }

    @Test
    void validateUpdate_shouldPassWhenAllFieldsAreUnique() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForUniquenessValidation();

        stubUniqueUpdateFields();

        assertDoesNotThrow(
                () -> validator.validateUpdate(request)
        );

        verify(employeeRepository)
                .existsByNicAndIdNot(
                        "199012345678",
                        1
                );

        verify(employeeRepository)
                .existsByMobileAndIdNot(
                        "0771234567",
                        1
                );

        verify(employeeRepository)
                .existsByEmergencycontactAndIdNot(
                        "0777654321",
                        1
                );

        verify(employeeRepository)
                .existsByEmergencycontactAndIdNot(
                        "0771234567",
                        1
                );

        verify(employeeRepository)
                .existsByMobileAndIdNot(
                        "0777654321",
                        1
                );
    }


    // =========================
    // UPDATE - PARTIAL REQUESTS
    // =========================

    @Test
    void validateUpdate_shouldSkipDesignationValidationWhenDesignationFieldsAreMissing() {

        EmployeeUpdateRequestDto request =
                mock(
                        EmployeeUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment())
                .thenReturn(null);

        when(request.getDesignation())
                .thenReturn(null);

        when(request.getGender())
                .thenReturn(null);

        when(request.getEmployeetype())
                .thenReturn(null);

        when(request.getDoj())
                .thenReturn(null);

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        when(request.getId())
                .thenReturn(1);

        stubUniqueUpdateFields();

        assertDoesNotThrow(
                () -> validator.validateUpdate(request)
        );
    }


    // =========================
    // HELPERS
    // =========================

    private EmployeeCreateRequestDto createCreateRequestForEmploymentValidation() {

        EmployeeCreateRequestDto request =
                mock(
                        EmployeeCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("operations");

        when(request.getDesignation().getName())
                .thenReturn("conductor");

        return request;
    }

    private EmployeeCreateRequestDto createCreateRequestForGenderNicValidation() {

        EmployeeCreateRequestDto request =
                mock(
                        EmployeeCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("operations");

        when(request.getDesignation().getName())
                .thenReturn("conductor");

        when(request.getEmployeetype().getName())
                .thenReturn("permanent");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear() - 1,
                                1,
                                1
                        )
                );

        return request;
    }

    private EmployeeCreateRequestDto createCreateRequestForUniquenessValidation() {

        EmployeeCreateRequestDto request =
                mock(
                        EmployeeCreateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("operations");

        when(request.getDesignation().getName())
                .thenReturn("conductor");

        when(request.getEmployeetype().getName())
                .thenReturn("permanent");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear() - 1,
                                1,
                                1
                        )
                );

        when(request.getGender().getName())
                .thenReturn("male");

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        return request;
    }

    private EmployeeCreateRequestDto createCreateRequestForFullValidation() {

        EmployeeCreateRequestDto request =
                createCreateRequestForUniquenessValidation();

        stubUniqueEmployeeFields();

        return request;
    }


    private EmployeeUpdateRequestDto createUpdateRequestForDesignationValidation() {

        EmployeeUpdateRequestDto request =
                mock(
                        EmployeeUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("operations");

        when(request.getDesignation().getName())
                .thenReturn("driver");

        return request;
    }

    private EmployeeUpdateRequestDto createUpdateRequestForEmploymentValidation() {

        EmployeeUpdateRequestDto request =
                mock(
                        EmployeeUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("operations");

        when(request.getDesignation().getName())
                .thenReturn("conductor");

        return request;
    }

    private EmployeeUpdateRequestDto createUpdateRequestForGenderNicValidation() {

        EmployeeUpdateRequestDto request =
                mock(
                        EmployeeUpdateRequestDto.class,
                        RETURNS_DEEP_STUBS
                );

        when(request.getDepartment().getName())
                .thenReturn("operations");

        when(request.getDesignation().getName())
                .thenReturn("conductor");

        when(request.getEmployeetype().getName())
                .thenReturn("permanent");

        when(request.getDoj())
                .thenReturn(
                        LocalDate.of(
                                LocalDate.now().getYear() - 1,
                                1,
                                1
                        )
                );

        return request;
    }

    private EmployeeUpdateRequestDto createUpdateRequestForUniquenessValidation() {

        EmployeeUpdateRequestDto request =
                createUpdateRequestForGenderNicValidation();

        when(request.getGender().getName())
                .thenReturn("male");

        when(request.getNic())
                .thenReturn("199012345678");

        when(request.getMobile())
                .thenReturn("0771234567");

        when(request.getEmergencycontact())
                .thenReturn("0777654321");

        when(request.getId())
                .thenReturn(1);

        return request;
    }

    private EmployeeUpdateRequestDto createUpdateRequestForFullValidation() {

        return createUpdateRequestForUniquenessValidation();
    }


    private void stubUniqueEmployeeFields() {

        when(employeeRepository.existsByNic("199012345678"))
                .thenReturn(false);

        when(employeeRepository.existsByMobile("0771234567"))
                .thenReturn(false);

        when(employeeRepository.existsByEmergencycontact("0777654321"))
                .thenReturn(false);

        when(employeeRepository.existsByEmergencycontact("0771234567"))
                .thenReturn(false);

        when(employeeRepository.existsByMobile("0777654321"))
                .thenReturn(false);
    }

    private void stubUniqueUpdateFields() {

        when(employeeRepository.existsByNicAndIdNot(
                "199012345678",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByMobileAndIdNot(
                "0771234567",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByEmergencycontactAndIdNot(
                "0777654321",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByEmergencycontactAndIdNot(
                "0771234567",
                1
        )).thenReturn(false);

        when(employeeRepository.existsByMobileAndIdNot(
                "0777654321",
                1
        )).thenReturn(false);
    }
}