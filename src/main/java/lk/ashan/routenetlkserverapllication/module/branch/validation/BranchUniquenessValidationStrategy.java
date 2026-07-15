package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validation strategy to ensure the uniqueness of branch attributes.
 * This class validates that branch codes, names, emails, telephones, and addresses
 * are unique during creation and update operations.
 */
@Component
@RequiredArgsConstructor
public class BranchUniquenessValidationStrategy implements BranchValidationStrategy {

    private final BranchRepository branchRepository;

    /**
     * Validates the uniqueness of branch attributes during creation.
     *
     * @param context the context containing branch details to validate
     * @throws ResourceExistsException if any branch attribute (code, name, email, telephone, or address) already exists
     */
    @Override
    public void validateCreate(BranchContext context) {
        if (branchRepository.existsByCodeEqualsIgnoreCase(context.getCode())) {
            throw new ResourceExistsException("Branch code already exists.");
        }
        if (branchRepository.existsByNameEqualsIgnoreCase(context.getName())) {
            throw new ResourceExistsException("Branch name already exists.");
        }
        if (branchRepository.existsByEmailEqualsIgnoreCase(context.getEmail())) {
            throw new ResourceExistsException("Branch email already exists.");
        }
        if (branchRepository.existsByTelephone(context.getTelephone())) {
            throw new ResourceExistsException("Branch telephone already exists.");
        }

        if (branchRepository.existsByAddressEqualsIgnoreCase(context.getAddress())) {
            throw new ResourceExistsException("Address already exists.");
        }
    }

    /**
     * Validates the uniqueness of branch attributes during updates.
     *
     * @param context the context containing branch details to validate
     * @throws ResourceExistsException if any branch attribute (name, email, telephone, or address) is already used by another branch
     */
    @Override
    public void validateUpdate(BranchContext context) {
        Integer id = context.getId();
        if (branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(context.getName(), id)) {
            throw new ResourceExistsException("Another branch already uses this name.");
        }
        if (branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(context.getEmail(), id)) {
            throw new ResourceExistsException("Another branch already uses this email.");
        }
        if (branchRepository.existsByTelephoneAndIdNot(context.getTelephone(), id)) {
            throw new ResourceExistsException("Another branch already uses this telephone.");
        }
        if (branchRepository.existsByAddressEqualsIgnoreCaseAndIdNot(context.getAddress(),id)) {
            throw new ResourceExistsException("Another branch in this address.");
        }
    }
}
