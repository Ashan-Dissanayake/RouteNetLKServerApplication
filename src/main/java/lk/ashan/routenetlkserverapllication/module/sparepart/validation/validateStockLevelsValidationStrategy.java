package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class validateStockLevelsValidationStrategy implements PartCreationStrategy {

    @Override
    public void validate(PartContext context) {

        if (context.getMaxlevel() == null || context.getRop() == null || context.getQoh() == null) {
            return;
        }

        if (context.getMaxlevel().compareTo(context.getRop()) <= 0) {
            throw new BusinessRuleViolationException(
                    "Max level must be greater than reorder point"
            );
        }

        if (context.getQoh().compareTo(context.getMaxlevel()) > 0) {
            throw new BusinessRuleViolationException(
                    "Quantity on hand cannot exceed maximum level"
            );
        }

        // Rule 3 — UPDATE scenario
        if (context.getExistingQoh() != null &&
                context.getExistingQoh()
                        .compareTo(context.getMaxlevel()) > 0) {

            throw new BusinessRuleViolationException(
                    "Max level cannot be less than current stock"
            );
        }

    }
}
