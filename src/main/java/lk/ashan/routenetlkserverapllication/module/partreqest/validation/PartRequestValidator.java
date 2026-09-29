package lk.ashan.routenetlkserverapllication.module.partreqest.validation;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PartRequestValidator {

    private final PartRequestRepository partRequestRepository;

    public void validateCreate(
            PartRequestCreateRequestDto dto
    ) {
        validateItems(dto.getPartrequestitems());
        validateDuplicateItems(dto.getPartrequestitems());
        validateDuplicateOpenRequests(dto);
    }

    private void validateItems(
            List<PartRequestItemDto> items
    ) {
        if (items == null || items.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "Request must contain at least one part"
            );
        }

        items.forEach(item -> {

            if (item.getQuantity() == null
                    || item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {

                throw new BusinessRuleViolationException(
                        "Requested quantity must be greater than zero for part: "
                                + item.getPart().getName()
                );
            }
        });
    }

    private void validateDuplicateItems(
            List<PartRequestItemDto> items
    ) {
        Set<Integer> partIds = new HashSet<>();

        for (PartRequestItemDto item : items) {

            if (!partIds.add(item.getPart().getId())) {

                throw new BusinessRuleViolationException(
                        "Duplicate part in the same request: "
                                + item.getPart().getName()
                );
            }
        }
    }

    private void validateDuplicateOpenRequests(
            PartRequestCreateRequestDto dto
    ) {
        for (PartRequestItemDto item : dto.getPartrequestitems()) {

            boolean exists =
                    partRequestRepository
                            .existsByBranchAndPartAndStatusInAndDoRequested(
                                    dto.getBranch().getId(),
                                    item.getPart().getId(),
                                    List.of("Pending", "Approved"),
                                    dto.getDorequested()
                            );

            if (exists) {

                throw new BusinessRuleViolationException(
                        "Part "
                                + item.getPart().getName()
                                + " has already been requested and is still pending."
                );
            }
        }
    }

    public void validateUpdate(
            PartRequest request,
            PartRequestUpdateRequestDto dto
    ) {
        String currentStatus =
                request.getPartrequeststatus().getName();

        if (!"PENDING".equalsIgnoreCase(currentStatus)) {

            throw new InvalidStateTransitionException(
                    "Only PENDING requests can be updated"
            );
        }

        validateItems(dto.getPartrequestitems());
    }

    public void validateStatusTransition(
            PartRequestStatus currentStatus,
            PartRequestStatus targetStatus
    ) {
        String current =
                normalize(currentStatus.getName());

        String target =
                normalize(targetStatus.getName());

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "PENDING" ->
                    target.equals("APPROVED")
                            || target.equals("REJECTED");

            case "APPROVED" ->
                    target.equals("COMPLETED");

            case "REJECTED",
                 "COMPLETED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {

            throw new BusinessRuleViolationException(
                    "Invalid part request status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    public void validateInitialStatus(
            PartRequestStatus status
    ) {
        if (!"PENDING".equalsIgnoreCase(status.getName())) {

            throw new BusinessRuleViolationException(
                    "New part requests must start with PENDING status"
            );
        }
    }

    private String normalize(String status) {
        return status.trim().toUpperCase();
    }
}