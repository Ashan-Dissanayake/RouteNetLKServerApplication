package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessException;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class DriverLicenseMedicalValidationStrategy implements DriverValidationStrategy {

    private final DriverRepository driverRepository;

    @Override
    public void validateCreate(DriverCreateRequestDto request) {
        validateLicenseDates(request.getDolicenseissued(), request.getDolicenseexpired());
        validateMedicalDates(request.getDomedicalissued(), request.getDomedicalexpired());
    }

    @Override
    public void validateUpdate(DriverUpdateRequestDto request) {
        // Fetch existing driver to check for License Category changes if any logic depends on it
        Driver existingDriver = driverRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        validateLicenseCategoryChange(existingDriver, request);
        validateLicenseDates(request.getDolicenseissued(), request.getDolicenseexpired());
        validateMedicalDates(request.getDomedicalissued(), request.getDomedicalexpired());
    }

    private void validateLicenseDates(LocalDate issued, LocalDate expiry) {
        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException("License issued date cannot be in the future");
        }
        // Expiry can be in the past (expired license), but logic usually requires valid license for active drivers.
        // The original code enforced expiry > issued.
        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleViolationException("License expiry must be after issued date");
        }

        long years = ChronoUnit.YEARS.between(issued, expiry);
        if (years > 4) {
            throw new BusinessRuleViolationException("Invalid license validity period (Max 4 years)");
        }
    }

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

    private void validateLicenseCategoryChange(Driver existingDriver, DriverUpdateRequestDto request) {
        if (request.getLicensecategory() == null) {
            throw new IllegalArgumentException("New license category cannot be null");
        }

        if (existingDriver.getLicensecategory().getId().equals(request.getLicensecategory().getId())) {
            return;
        }

        if (!existingDriver.getDolicenseexpired().isBefore(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    "License category can only be changed when the existing license is expired"
            );
        }
    }
}
