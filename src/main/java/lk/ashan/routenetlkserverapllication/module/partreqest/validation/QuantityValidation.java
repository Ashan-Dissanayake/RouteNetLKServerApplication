package lk.ashan.routenetlkserverapllication.module.partreqest.validation;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class QuantityValidation implements PartRequestValidationStrategy {
    @Override
    public void validate(PartRequestValidationContext context) {
        context.getItems().forEach(item -> {
            if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleViolationException(
                        "Requested quantity must be greater than zero for part: " + item.getPart().getName()
                );
            }
        });
    }
}
