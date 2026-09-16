package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchValidatorTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchValidator validator;


    // =========================
    // CREATE
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenCodeExists() {

        BranchCreateRequestDto request =
                BranchCreateRequestDto.builder()
                        .code("BR001")
                        .name("Branch A")
                        .email("brancha@example.com")
                        .telephone("123456789")
                        .address("123 Main St")
                        .build();

        when(branchRepository.existsByCodeEqualsIgnoreCase("BR001"))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(branchRepository)
                .existsByCodeEqualsIgnoreCase("BR001");
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenNameExists() {

        BranchCreateRequestDto request =
                BranchCreateRequestDto.builder()
                        .code("BR001")
                        .name("Branch A")
                        .email("brancha@example.com")
                        .telephone("123456789")
                        .address("123 Main St")
                        .build();

        when(branchRepository.existsByCodeEqualsIgnoreCase("BR001"))
                .thenReturn(false);

        when(branchRepository.existsByNameEqualsIgnoreCase("Branch A"))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(branchRepository)
                .existsByCodeEqualsIgnoreCase("BR001");

        verify(branchRepository)
                .existsByNameEqualsIgnoreCase("Branch A");
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenEmailExists() {

        BranchCreateRequestDto request =
                BranchCreateRequestDto.builder()
                        .code("BR001")
                        .name("Branch A")
                        .email("brancha@example.com")
                        .telephone("123456789")
                        .address("123 Main St")
                        .build();

        when(branchRepository.existsByCodeEqualsIgnoreCase("BR001"))
                .thenReturn(false);

        when(branchRepository.existsByNameEqualsIgnoreCase("Branch A"))
                .thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCase(
                "brancha@example.com"
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(branchRepository)
                .existsByEmailEqualsIgnoreCase(
                        "brancha@example.com"
                );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenTelephoneExists() {

        BranchCreateRequestDto request =
                BranchCreateRequestDto.builder()
                        .code("BR001")
                        .name("Branch A")
                        .email("brancha@example.com")
                        .telephone("123456789")
                        .address("123 Main St")
                        .build();

        when(branchRepository.existsByCodeEqualsIgnoreCase("BR001"))
                .thenReturn(false);

        when(branchRepository.existsByNameEqualsIgnoreCase("Branch A"))
                .thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCase(
                "brancha@example.com"
        )).thenReturn(false);

        when(branchRepository.existsByTelephone("123456789"))
                .thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(branchRepository)
                .existsByTelephone("123456789");
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenAddressExists() {

        BranchCreateRequestDto request =
                BranchCreateRequestDto.builder()
                        .code("BR001")
                        .name("Branch A")
                        .email("brancha@example.com")
                        .telephone("123456789")
                        .address("123 Main St")
                        .build();

        when(branchRepository.existsByCodeEqualsIgnoreCase("BR001"))
                .thenReturn(false);

        when(branchRepository.existsByNameEqualsIgnoreCase("Branch A"))
                .thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCase(
                "brancha@example.com"
        )).thenReturn(false);

        when(branchRepository.existsByTelephone("123456789"))
                .thenReturn(false);

        when(branchRepository.existsByAddressEqualsIgnoreCase(
                "123 Main St"
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateCreate(request)
        );

        verify(branchRepository)
                .existsByAddressEqualsIgnoreCase("123 Main St");
    }

    @Test
    void validateCreate_shouldNotThrowExceptionWhenAttributesAreUnique() {

        BranchCreateRequestDto request =
                BranchCreateRequestDto.builder()
                        .code("BR002")
                        .name("Branch B")
                        .email("branchb@example.com")
                        .telephone("987654321")
                        .address("456 Elm St")
                        .build();

        when(branchRepository.existsByCodeEqualsIgnoreCase("BR002"))
                .thenReturn(false);

        when(branchRepository.existsByNameEqualsIgnoreCase("Branch B"))
                .thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCase(
                "branchb@example.com"
        )).thenReturn(false);

        when(branchRepository.existsByTelephone("987654321"))
                .thenReturn(false);

        when(branchRepository.existsByAddressEqualsIgnoreCase(
                "456 Elm St"
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateCreate(request)
        );

        verify(branchRepository)
                .existsByCodeEqualsIgnoreCase("BR002");

        verify(branchRepository)
                .existsByNameEqualsIgnoreCase("Branch B");

        verify(branchRepository)
                .existsByEmailEqualsIgnoreCase(
                        "branchb@example.com"
                );

        verify(branchRepository)
                .existsByTelephone("987654321");

        verify(branchRepository)
                .existsByAddressEqualsIgnoreCase(
                        "456 Elm St"
                );
    }


    // =========================
    // UPDATE
    // =========================

    @Test
    void validateUpdate_shouldThrowExceptionWhenNameExistsForAnotherBranch() {

        BranchUpdateRequestDto request =
                BranchUpdateRequestDto.builder()
                        .id(1)
                        .name("Branch C")
                        .email("branchc@example.com")
                        .telephone("123456789")
                        .address("456 Elm St")
                        .build();

        when(branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                "Branch C",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(request)
        );

        verify(branchRepository)
                .existsByNameEqualsIgnoreCaseAndIdNot(
                        "Branch C",
                        1
                );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenEmailExistsForAnotherBranch() {

        BranchUpdateRequestDto request =
                BranchUpdateRequestDto.builder()
                        .id(1)
                        .name("Branch C")
                        .email("branchc@example.com")
                        .telephone("123456789")
                        .address("456 Elm St")
                        .build();

        when(branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                "Branch C",
                1
        )).thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(
                "branchc@example.com",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(request)
        );

        verify(branchRepository)
                .existsByNameEqualsIgnoreCaseAndIdNot(
                        "Branch C",
                        1
                );

        verify(branchRepository)
                .existsByEmailEqualsIgnoreCaseAndIdNot(
                        "branchc@example.com",
                        1
                );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenTelephoneExistsForAnotherBranch() {

        BranchUpdateRequestDto request =
                BranchUpdateRequestDto.builder()
                        .id(1)
                        .name("Branch C")
                        .email("branchc@example.com")
                        .telephone("123456789")
                        .address("456 Elm St")
                        .build();

        when(branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                "Branch C",
                1
        )).thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(
                "branchc@example.com",
                1
        )).thenReturn(false);

        when(branchRepository.existsByTelephoneAndIdNot(
                "123456789",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(request)
        );

        verify(branchRepository)
                .existsByNameEqualsIgnoreCaseAndIdNot(
                        "Branch C",
                        1
                );

        verify(branchRepository)
                .existsByEmailEqualsIgnoreCaseAndIdNot(
                        "branchc@example.com",
                        1
                );

        verify(branchRepository)
                .existsByTelephoneAndIdNot(
                        "123456789",
                        1
                );
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenAddressExistsForAnotherBranch() {

        BranchUpdateRequestDto request =
                BranchUpdateRequestDto.builder()
                        .id(1)
                        .name("Branch C")
                        .email("branchc@example.com")
                        .telephone("123456789")
                        .address("456 Elm St")
                        .build();

        when(branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                "Branch C",
                1
        )).thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(
                "branchc@example.com",
                1
        )).thenReturn(false);

        when(branchRepository.existsByTelephoneAndIdNot(
                "123456789",
                1
        )).thenReturn(false);

        when(branchRepository.existsByAddressEqualsIgnoreCaseAndIdNot(
                "456 Elm St",
                1
        )).thenReturn(true);

        assertThrows(
                ResourceExistsException.class,
                () -> validator.validateUpdate(request)
        );

        verify(branchRepository)
                .existsByAddressEqualsIgnoreCaseAndIdNot(
                        "456 Elm St",
                        1
                );
    }

    @Test
    void validateUpdate_shouldNotThrowExceptionWhenAttributesAreUnique() {

        BranchUpdateRequestDto request =
                BranchUpdateRequestDto.builder()
                        .id(2)
                        .name("Branch D")
                        .email("branchd@example.com")
                        .telephone("987654321")
                        .address("456 Elm St")
                        .build();

        when(branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                "Branch D",
                2
        )).thenReturn(false);

        when(branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(
                "branchd@example.com",
                2
        )).thenReturn(false);

        when(branchRepository.existsByTelephoneAndIdNot(
                "987654321",
                2
        )).thenReturn(false);

        when(branchRepository.existsByAddressEqualsIgnoreCaseAndIdNot(
                "456 Elm St",
                2
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> validator.validateUpdate(request)
        );

        verify(branchRepository)
                .existsByNameEqualsIgnoreCaseAndIdNot(
                        "Branch D",
                        2
                );

        verify(branchRepository)
                .existsByEmailEqualsIgnoreCaseAndIdNot(
                        "branchd@example.com",
                        2
                );

        verify(branchRepository)
                .existsByTelephoneAndIdNot(
                        "987654321",
                        2
                );

        verify(branchRepository)
                .existsByAddressEqualsIgnoreCaseAndIdNot(
                        "456 Elm St",
                        2
                );
    }
}