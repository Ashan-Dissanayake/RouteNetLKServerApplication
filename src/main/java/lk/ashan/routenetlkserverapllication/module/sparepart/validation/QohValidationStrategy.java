package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QohValidationStrategy implements PartCreationStrategy {

    @Override
    public void validate(PartCreationContext context) {

        if (context.getQoh() == null ||
                context.getQoh().compareTo(BigDecimal.ZERO) < 0) {

            throw new BusinessRuleViolationException(
                    "Quantity on hand cannot be negative"
            );
        }
    }
}
