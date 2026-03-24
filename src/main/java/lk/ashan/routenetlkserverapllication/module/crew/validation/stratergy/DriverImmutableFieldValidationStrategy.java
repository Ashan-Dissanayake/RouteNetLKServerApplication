package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class DriverImmutableFieldValidationStrategy
        implements DriverValidationStrategy {

    @Override
    public void validateCreate(DriverValidationContext context) {

        // nothing to validate during create

    }

    @Override
    public void validateUpdate(DriverValidationContext context) {
        validateLicenseNumber(context);

        validateEmployee(context);

        validateLicenseIssuedDate(context);

    }

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
