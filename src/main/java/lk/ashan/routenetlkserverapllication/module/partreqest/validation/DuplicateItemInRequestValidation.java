package lk.ashan.routenetlkserverapllication.module.partreqest.validation;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DuplicateItemInRequestValidation implements PartRequestValidationStrategy {
    @Override
    public void validate(PartRequestValidationContext context) {
        Set<Integer> partIds = new HashSet<>();
        for (PartRequestItemDto item : context.getItems()) {
            if (!partIds.add(item.getPart().getId())) {
                throw new BusinessRuleViolationException(
                        "Duplicate part in the same request: " + item.getPart().getName()
                );
            }
        }
    }
}
