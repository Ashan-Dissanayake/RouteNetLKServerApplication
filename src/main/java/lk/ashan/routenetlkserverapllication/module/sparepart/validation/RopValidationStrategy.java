package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RopValidationStrategy implements PartCreationStrategy {

    @Override
    public void validate(PartCreationContext context) {

        if (context.getRop() == null ||
                context.getRop().compareTo(BigDecimal.ZERO) < 0) {

            throw new BusinessRuleViolationException(
                    "Reorder point cannot be negative"
            );
        }
    }
}
