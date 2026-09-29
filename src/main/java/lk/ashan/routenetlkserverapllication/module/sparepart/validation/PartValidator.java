package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PartValidator {

    public void validateCreate(
            PartCreateRequestDto request
    ) {
        validateStockLevels(
                request.getQoh(),
                request.getRop(),
                request.getMaxlevel()
        );
    }

    public void validateUpdate(
            PartUpdateRequestDto request,
            Part existingPart
    ) {
        validateStockLevels(
                request.getQoh(),
                request.getRop(),
                request.getMaxlevel()
        );

        validateMaxLevelAgainstCurrentStock(
                request.getMaxlevel(),
                existingPart.getQoh()
        );
    }

    public void validateInitialStatus(
            Partstatus status
    ) {
        if (!"AVAILABLE".equalsIgnoreCase(status.getName())) {
            throw new BusinessRuleViolationException(
                    "New parts must start with AVAILABLE status"
            );
        }
    }

    public void validateStatusTransition(
            Partstatus currentStatus,
            Partstatus targetStatus
    ) {
        String current =
                normalize(currentStatus.getName());

        String target =
                normalize(targetStatus.getName());

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "AVAILABLE" ->
                    target.equals("LOW STOCK")
                            || target.equals("OUT OF STOCK")
                            || target.equals("DECOMMISSIONED");

            case "LOW STOCK" ->
                    target.equals("AVAILABLE")
                            || target.equals("OUT OF STOCK")
                            || target.equals("DECOMMISSIONED");

            case "OUT OF STOCK" ->
                    target.equals("LOW STOCK")
                            || target.equals("AVAILABLE")
                            || target.equals("DECOMMISSIONED");

            case "DECOMMISSIONED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new BusinessRuleViolationException(
                    "Invalid part status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    public void validateDeactivation(
            List<Part> parts
    ) {
        parts.stream()
                .filter(part ->
                        part.getPartstatus() != null
                                && "DECOMMISSIONED".equalsIgnoreCase(
                                part.getPartstatus().getName()
                        )
                )
                .findFirst()
                .ifPresent(part -> {
                    throw new BusinessRuleViolationException(
                            String.format(
                                    "%s parts cannot be deleted. Part ID: %d",
                                    part.getPartstatus().getName(),
                                    part.getId()
                            )
                    );
                });
    }

    private void validateStockLevels(
            BigDecimal qoh,
            BigDecimal rop,
            BigDecimal maxLevel
    ) {
        if (maxLevel == null
                || rop == null
                || qoh == null) {
            return;
        }

        if (maxLevel.compareTo(rop) <= 0) {
            throw new BusinessRuleViolationException(
                    "Max level must be greater than reorder point"
            );
        }

        if (qoh.compareTo(maxLevel) > 0) {
            throw new BusinessRuleViolationException(
                    "Quantity on hand cannot exceed maximum level"
            );
        }
    }

    private void validateMaxLevelAgainstCurrentStock(
            BigDecimal maxLevel,
            BigDecimal existingQoh
    ) {
        if (maxLevel == null
                || existingQoh == null) {
            return;
        }

        if (existingQoh.compareTo(maxLevel) > 0) {
            throw new BusinessRuleViolationException(
                    "Max level cannot be less than current stock"
            );
        }
    }

    private String normalize(String value) {
        return value.trim().toUpperCase();
    }
}