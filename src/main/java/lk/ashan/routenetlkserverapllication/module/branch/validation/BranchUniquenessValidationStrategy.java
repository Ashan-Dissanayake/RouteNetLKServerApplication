package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchUniquenessValidationStrategy implements BranchValidationStrategy {

    private final BranchRepository branchRepository;

    @Override
    public void validateCreate(BranchContext context) {
        if (branchRepository.existsByCode(context.getCode())) {
            throw new ResourceExistsException("Branch code already exists.");
        }
        if (branchRepository.existsByName(context.getName())) {
            throw new ResourceExistsException("Branch name already exists.");
        }
        if (branchRepository.existsByEmail(context.getEmail())) {
            throw new ResourceExistsException("Branch email already exists.");
        }
        if (branchRepository.existsByTelephone(context.getTelephone())) {
            throw new ResourceExistsException("Branch telephone already exists.");
        }
    }

    @Override
    public void validateUpdate(BranchContext context) {
        Integer id = context.getId();
        if (branchRepository.existsByCodeAndIdNot(context.getCode(), id)) {
            throw new ResourceExistsException("Another branch already uses this code.");
        }
        if (branchRepository.existsByNameAndIdNot(context.getName(), id)) {
            throw new ResourceExistsException("Another branch already uses this name.");
        }
        if (branchRepository.existsByEmailAndIdNot(context.getEmail(), id)) {
            throw new ResourceExistsException("Another branch already uses this email.");
        }
        if (branchRepository.existsByTelephoneAndIdNot(context.getTelephone(), id)) {
            throw new ResourceExistsException("Another branch already uses this telephone.");
        }
    }
}
