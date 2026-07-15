package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Validation strategy for ensuring the medical details of a conductor are valid.
 * This strategy checks the validity of the medical certificate's issue and expiry dates.
 */
@Component
public class ConductorMedicalValidationStrategy implements ConductorValidationStrategy {

    /**
     * Validates the medical details during the creation of a conductor.
     *
     * @param context the validation context containing the medical details
     * @throws BusinessRuleViolationException if the medical expiry is not in the future
     *                                        or if the validity exceeds 6 months
     */
    @Override
    public void validateCreate(ConductorValidationContext context) {
        validate(context.getMedicalIssued(), context.getMedicalExpired());
    }

    /**
     * Validates the medical details during the update of a conductor.
     *
     * @param context the validation context containing the medical details
     * @throws BusinessRuleViolationException if the medical expiry is not in the future
     *                                        or if the validity exceeds 6 months
     */
    @Override
    public void validateUpdate(ConductorValidationContext context) {
        validate(context.getMedicalIssued(), context.getMedicalExpired());
    }

    /**
     * Validates the issue and expiry dates of the medical certificate.
     *
     * @param issued the date the medical certificate was issued
     * @param expiry the date the medical certificate expires
     * @throws BusinessRuleViolationException if the expiry date is not in the future
     *                                        or if the validity exceeds 6 months
     */
    private void validate(LocalDate issued, LocalDate expiry) {
        if (!expiry.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("Medical expiry must be in the future");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);

        if (months > 6) {
            throw new BusinessRuleViolationException("Medical validity cannot exceed 6 months");
        }
    }
}
