package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PermitDateValidationStrategy implements PermitValidationStrategy {

    @Override
    public void validate(PermitValidationContext context) {
        LocalDate issued = context.getDoissued();
        LocalDate expired = context.getDoexpired();

        //doissued ≤ today
        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("Issued date cannot be in the future");
        }

        // doexpired > doissued
        if (!expired.isAfter(issued)) {
            throw new BusinessRuleViolationException("Expiry date must be after issued date");
        }
    }
}
