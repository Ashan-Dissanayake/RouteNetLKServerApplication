package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BranchValidator {

    private final BranchRepository branchRepository;

    public void validateCreate(BranchCreateRequestDto request) {

        if (branchRepository.existsByCodeEqualsIgnoreCase(request.getCode())) {
            throw new ResourceExistsException("Branch code already exists.");
        }

        if (branchRepository.existsByNameEqualsIgnoreCase(request.getName())) {
            throw new ResourceExistsException("Branch name already exists.");
        }

        if (branchRepository.existsByEmailEqualsIgnoreCase(request.getEmail())) {
            throw new ResourceExistsException("Branch email already exists.");
        }

        if (branchRepository.existsByTelephone(request.getTelephone())) {
            throw new ResourceExistsException("Branch telephone already exists.");
        }

        if (branchRepository.existsByAddressEqualsIgnoreCase(request.getAddress())) {
            throw new ResourceExistsException("Address already exists.");
        }
    }

    public void validateUpdate(BranchUpdateRequestDto request) {

        Integer id = request.getId();

        if (branchRepository.existsByNameEqualsIgnoreCaseAndIdNot(
                request.getName(), id)) {
            throw new ResourceExistsException(
                    "Another branch already uses this name.");
        }

        if (branchRepository.existsByEmailEqualsIgnoreCaseAndIdNot(
                request.getEmail(), id)) {
            throw new ResourceExistsException(
                    "Another branch already uses this email.");
        }

        if (branchRepository.existsByTelephoneAndIdNot(
                request.getTelephone(), id)) {
            throw new ResourceExistsException(
                    "Another branch already uses this telephone.");
        }

        if (branchRepository.existsByAddressEqualsIgnoreCaseAndIdNot(
                request.getAddress(), id)) {
            throw new ResourceExistsException(
                    "Another branch uses this address.");
        }
    }


    public void validateStatusTransition(BranchStatus currentStatus, BranchStatus targetStatus) {

        String current = currentStatus.getName().toUpperCase();
        String target = targetStatus.getName().toUpperCase();

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {
            case "ACTIVE" -> target.equals("SUSPENDED");

            case "SUSPENDED" ->
                    target.equals("ACTIVE") || target.equals("CLOSED");

            case "CLOSED" -> false;

            default -> false;
        };

        if (!valid) {
            throw new BusinessRuleViolationException(
                    "Invalid branch status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    public void validateInitialStatus(BranchStatus status) {

        String statusName = status.getName().toUpperCase();

        if (!statusName.equals("ACTIVE")) {
            throw new BusinessRuleViolationException(
                    statusName + " cannot be used as an initial branch status."
            );
        }
    }
}