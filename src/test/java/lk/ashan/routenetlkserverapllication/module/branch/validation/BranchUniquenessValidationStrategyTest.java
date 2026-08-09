package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BranchUniquenessValidationStrategyTest {

    @Mock
    private BranchRepository branchRepository;

    private BranchUniquenessValidationStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new BranchUniquenessValidationStrategy(branchRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenCodeExists() {
        BranchContext context = BranchContext.builder().code("BR001").build();
        when(branchRepository.existsByCodeEqualsIgnoreCase("BR001")).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> strategy.validateCreate(context));
        verify(branchRepository).existsByCodeEqualsIgnoreCase("BR001");
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenNameExists() {
        BranchContext context = BranchContext.builder().name("Branch A").build();
        when(branchRepository.existsByNameEqualsIgnoreCase("Branch A")).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> strategy.validateCreate(context));
        verify(branchRepository).existsByNameEqualsIgnoreCase("Branch A");
    }

    @Test
    void validateCreate_shouldNotThrowExceptionWhenAttributesAreUnique() {
        BranchContext context = BranchContext.builder().
        code("BR002").
        name("Branch B").
        email("branchb@example.com").
        telephone("123456789").
        address("123 Main St").build();

        when(branchRepository.existsByCodeEqualsIgnoreCase("BR002")).thenReturn(false);
        when(branchRepository.existsByNameEqualsIgnoreCase("Branch B")).thenReturn(false);
        when(branchRepository.existsByEmailEqualsIgnoreCase("branchb@example.com")).thenReturn(false);
        when(branchRepository.existsByTelephone("123456789")).thenReturn(false);
        when(branchRepository.existsByAddressEqualsIgnoreCase("123 Main St")).thenReturn(false);

        strategy.validateCreate(context);

        verify(branchRepository).existsByCodeEqualsIgnoreCase("BR002");
        verify(branchRepository).existsByNameEqualsIgnoreCase("Branch B");
        verify(branchRepository).existsByEmailEqualsIgnoreCase("branchb@example.com");
        verify(branchRepository).existsByTelephone("123456789");
        verify(branchRepository).existsByAddressEqualsIgnoreCase("123 Main St");
    }

    @Test
    void validateUpdate_shouldThrowExceptionWhenNameExistsForAnotherBranch() {
        BranchContext context = BranchContext.builder().
                id(1).
        name("Branch C").build();
        when(branchRepository.existsByNameEqualsIgnoreCaseAndIdNot("Branch C", 1)).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> strategy.validateUpdate(context));
        verify(branchRepository).existsByNameEqualsIgnoreCaseAndIdNot("Branch C", 1);
    }

    @Test
    void validateUpdate_shouldNotThrowExceptionWhenAttributesAreUnique() {
        BranchContext context = BranchContext.builder().
        id(2).
        name("Branch D").
        email("branchd@example.com").
        telephone("987654321").
        address("456 Elm St").build();

        when(branchRepository.existsByNameEqualsIgnoreCaseAndIdNot("Branch D", 2)).thenReturn(false);
        when(branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot("branchd@example.com", 2)).thenReturn(false);
        when(branchRepository.existsByTelephoneAndIdNot("987654321", 2)).thenReturn(false);
        when(branchRepository.existsByAddressEqualsIgnoreCaseAndIdNot("456 Elm St", 2)).thenReturn(false);

        strategy.validateUpdate(context);

        verify(branchRepository).existsByNameEqualsIgnoreCaseAndIdNot("Branch D", 2);
        verify(branchRepository).existsByEmailEqualsIgnoreCaseAndIdNot("branchd@example.com", 2);
        verify(branchRepository).existsByTelephoneAndIdNot("987654321", 2);
        verify(branchRepository).existsByAddressEqualsIgnoreCaseAndIdNot("456 Elm St", 2);
    }
}
