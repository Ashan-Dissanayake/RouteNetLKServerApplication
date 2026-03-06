package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class RopLogicValidationStrategy implements PartCreationStrategy {

    @Override
    public void validate(PartCreationContext context) {

        if (context.getRop().compareTo(context.getQoh()) > 0) {

            throw new BusinessRuleViolationException(
                    "Reorder point cannot be greater than quantity on hand"
            );
        }
    }
}
