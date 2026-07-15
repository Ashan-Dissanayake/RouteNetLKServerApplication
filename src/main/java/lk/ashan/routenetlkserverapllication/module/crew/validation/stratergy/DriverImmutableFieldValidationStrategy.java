package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

/**
 * Validation strategy for ensuring that certain fields of a Driver entity
 * remain immutable during updates. Implements the {@link DriverValidationStrategy}.
 */
@Component
public class DriverImmutableFieldValidationStrategy
        implements DriverValidationStrategy {

    /**
     * Validates the creation of a Driver entity.
     *
     * @param context the validation context containing the data for validation
     */
    @Override
    public void validateCreate(DriverValidationContext context) {

        // nothing to validate during create

    }

    /**
     * Validates the update of a Driver entity to ensure immutable fields are not modified.
     *
     * @param context the validation context containing the data for validation
     * @throws BusinessRuleViolationException if any immutable field is modified
     */
    @Override
    public void validateUpdate(DriverValidationContext context) {
        validateLicenseNumber(context);

        validateEmployee(context);

        validateLicenseIssuedDate(context);

    }

    /**
     * Validates that the license number has not been changed.
     *
     * @param context the validation context containing the data for validation
     * @throws BusinessRuleViolationException if the license number is modified
     */
    private void validateLicenseNumber(
            DriverValidationContext context
    ) {

        if (!context.getExistingLicenseNumber()
                .equalsIgnoreCase(context.getLicenseNumber())) {

            throw new BusinessRuleViolationException(
                    "License number cannot be changed"
            );

        }

    }

    /**
     * Validates that the employee has not been reassigned.
     *
     * @param context the validation context containing the data for validation
     * @throws BusinessRuleViolationException if the employee is reassigned
     */
    private void validateEmployee(
            DriverValidationContext context
    ) {

        if (!context.getExistingEmployeeId()
                .equals(context.getEmployeeId())) {

            throw new BusinessRuleViolationException(
                    "Employee cannot be reassigned"
            );

        }

    }

    /**
     * Validates that the license issued date has not been modified.
     *
     * @param context the validation context containing the data for validation
     * @throws BusinessRuleViolationException if the license issued date is modified
     */
    private void validateLicenseIssuedDate(
            DriverValidationContext context
    ) {

        if (!context.getExistingLicenseIssued()
                .equals(context.getLicenseIssued())) {

            throw new BusinessRuleViolationException(
                    "License issued date cannot be modified"
            );

        }

    }

}
