package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchUniquenessValidationStrategy implements BranchValidationStrategy {

    private final BranchRepository branchRepository;

    @Override
    public void validateCreate(BranchCreateRequestDto request) {
        if (branchRepository.existsByCode(request.getCode())) {
            throw new ResourceExistsException("Branch code already exists.");
        }
        if (branchRepository.existsByName(request.getName())) {
            throw new ResourceExistsException("Branch name already exists.");
        }
        if (branchRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("Branch email already exists.");
        }
        if (branchRepository.existsByTelephone(request.getTelephone())) {
            throw new ResourceExistsException("Branch telephone already exists.");
        }
    }

    @Override
    public void validateUpdate(BranchUpdateRequestDto request) {
        if (branchRepository.existsByCodeAndIdNot(request.getCode(), request.getId())) {
            throw new ResourceExistsException("Another branch already uses this code.");
        }
        if (branchRepository.existsByNameAndIdNot(request.getName(), request.getId())) {
            throw new ResourceExistsException("Another branch already uses this name.");
        }
        if (branchRepository.existsByEmailAndIdNot(request.getEmail(), request.getId())) {
            throw new ResourceExistsException("Another branch already uses this email.");
        }
        if (branchRepository.existsByTelephoneAndIdNot(request.getTelephone(), request.getId())) {
            throw new ResourceExistsException("Another branch already uses this telephone.");
        }
    }
}
