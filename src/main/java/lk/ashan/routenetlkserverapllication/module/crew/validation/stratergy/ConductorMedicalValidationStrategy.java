package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ConductorMedicalValidationStrategy implements ConductorValidationStrategy {

    @Override
    public void validateCreate(ConductorValidationContext context) {
        validate(context.getMedicalIssued(),context.getMedicalExpired() );
    }

    @Override
    public void validateUpdate(ConductorValidationContext context) {
        validate(context.getMedicalIssued(), context.getMedicalExpired());
    }

    private void validate(LocalDate issued,LocalDate expiry) {
        if (!expiry.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("Medical expiry must be in the future");
        }

        long months = ChronoUnit.MONTHS.between( issued,expiry);

        if (months > 6) {
            throw new BusinessRuleViolationException("Medical validity cannot exceed 6 months");
        }

    }

}
