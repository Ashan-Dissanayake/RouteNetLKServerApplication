package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Validation strategy for driver license and medical details.
 * Ensures that license and medical dates are valid and enforces business rules
 * for license category changes.
 */
@Component
@RequiredArgsConstructor
public class DriverLicenseMedicalValidationStrategy implements DriverValidationStrategy {

    private final DriverRepository driverRepository;

    /**
     * Validates the creation of a driver by checking license and medical dates.
     *
     * @param context the validation context containing driver details
     * @throws BusinessRuleViolationException if any validation rule is violated
     */
    @Override
    public void validateCreate(DriverValidationContext context) {

        validateLicenseDates(context.getLicenseIssued(), context.getLicenseExpired());
        validateMedicalDates(context.getMedicalIssued(), context.getMedicalExpired());

    }

    /**
     * Validates the update of a driver by checking license category changes,
     * license dates, and medical dates.
     *
     * @param context the validation context containing updated driver details
     * @throws ResourceNotFoundException if the driver is not found
     * @throws BusinessRuleViolationException if any validation rule is violated
     */
    @Override
    public void validateUpdate(DriverValidationContext context) {

        Driver existingDriver = driverRepository.findById(context.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        validateLicenseCategoryChange(existingDriver, context);

        validateLicenseDates(context.getLicenseIssued(), context.getLicenseExpired());
        validateMedicalDates(context.getMedicalIssued(), context.getMedicalExpired());

    }

    /**
     * Validates the license issued and expiry dates.
     *
     * @param issued the license issued date
     * @param expiry the license expiry date
     * @throws BusinessRuleViolationException if the dates are invalid
     */
    private void validateLicenseDates(LocalDate issued, LocalDate expiry) {

        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("License issued date cannot be in the future");
        }

        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleViolationException("License expiry must be after issued date");
        }

        long years = ChronoUnit.YEARS.between(issued, expiry);

        if (years > 4) {
            throw new BusinessRuleViolationException("Invalid license validity period (Max 4 years)");
        }
    }

    /**
     * Validates the medical issued and expiry dates.
     *
     * @param issued the medical issued date
     * @param expiry the medical expiry date
     * @throws BusinessRuleViolationException if the dates are invalid
     */
    private void validateMedicalDates(LocalDate issued, LocalDate expiry) {

        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("Medical issued date cannot be in the future");
        }

        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleViolationException("Medical expiry must be after issued date");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);

        if (months > 6) {
            throw new BusinessRuleViolationException("Medical validity cannot exceed 6 months");
        }
    }

    /**
     * Validates changes to the driver's license category.
     *
     * @param existingDriver the existing driver entity
     * @param context the validation context containing updated driver details
     * @throws IllegalArgumentException if the new license category is null
     * @throws BusinessRuleViolationException if the license category change violates business rules
     */
    private void validateLicenseCategoryChange(Driver existingDriver, DriverValidationContext context) {

        if (context.getLicenseCategoryName() == null) {
            throw new IllegalArgumentException("New license category cannot be null");
        }

        if (existingDriver.getLicensecategory().getName().equals(context.getLicenseCategoryName())) {
            return;
        }

        if (!existingDriver.getDolicenseexpired().isBefore(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    "License category can only be changed when the existing license is expired"
            );
        }
    }
}
