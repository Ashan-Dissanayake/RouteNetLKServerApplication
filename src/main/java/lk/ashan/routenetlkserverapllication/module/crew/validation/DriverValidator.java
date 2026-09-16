package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class DriverValidator {

    private final DriverRepository driverRepository;

    public void validateCreate(DriverCreateRequestDto dto) {validateCreateBusinessRules(dto);
        validateLicenseDates(dto.getDolicenseissued(), dto.getDolicenseexpired());
        validateMedicalDates(dto.getDomedicalissued(), dto.getDomedicalexpired());
        validateUniqueness(dto);
    }

    public void validateUpdate(Driver existing, DriverUpdateRequestDto dto) {
        validateImmutableFields(existing, dto);
        validateLicenseDates(dto.getDolicenseissued(), dto.getDolicenseexpired());
        validateMedicalDates(dto.getDomedicalissued(), dto.getDomedicalexpired());
        validateUniqueness(dto);
    }

    private void validateCreateBusinessRules(DriverCreateRequestDto dto) {

        if (!dto.getCrewstatus().getName()
                .equalsIgnoreCase("Eligible")) {

            throw new BusinessRuleViolationException(
                    "New driver must have status 'ELIGIBLE'");
        }

        if (!dto.getRoutefamiliaritylevel().getName()
                .equalsIgnoreCase("Low")) {

            throw new BusinessRuleViolationException(
                    "New driver route familiarity must have 'LOW'");
        }
    }

    private void validateImmutableFields(Driver existing, DriverUpdateRequestDto dto) {

        if (!existing.getLicensenumber()
                .equalsIgnoreCase(dto.getLicensenumber())) {

            throw new BusinessRuleViolationException(
                    "License number cannot be changed");
        }

        if (!existing.getEmployee().getId()
                .equals(dto.getEmployee().getId())) {

            throw new BusinessRuleViolationException(
                    "Employee cannot be reassigned");
        }

        if (!existing.getDolicenseissued()
                .equals(dto.getDolicenseissued())) {

            throw new BusinessRuleViolationException(
                    "License issued date cannot be modified");
        }
    }

    private void validateUniqueness(DriverCreateRequestDto dto) {

        if (driverRepository.existsByLicensenumber(
                dto.getLicensenumber())) {

            throw new ResourceExistsException(
                    "License number already exists");
        }

        if (driverRepository.existsByEmployeeId(
                dto.getEmployee().getId())) {

            throw new ResourceExistsException(
                    "A driver profile already exists for this employee");
        }
    }

    private void validateUniqueness(DriverUpdateRequestDto dto) {

        if (driverRepository.existsByLicensenumberAndIdNot(
                dto.getLicensenumber(),
                dto.getId())) {

            throw new ResourceExistsException(
                    "License number already exists");
        }

        if (driverRepository.existsByEmployeeIdAndIdNot(
                dto.getEmployee().getId(),
                dto.getId())) {

            throw new ResourceExistsException(
                    "A driver profile already exists for this employee");
        }
    }

    private void validateLicenseDates(LocalDate issued, LocalDate expiry) {

        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    "License issued date cannot be in the future");
        }

        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleViolationException(
                    "License expiry must be after issued date");
        }

        long years = ChronoUnit.YEARS.between(issued, expiry);

        if (years > 4) {
            throw new BusinessRuleViolationException(
                    "Invalid license validity period (Max 4 years)");
        }
    }

    private void validateMedicalDates(LocalDate issued, LocalDate expiry) {

        if (issued.isAfter(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    "Medical issued date cannot be in the future");
        }

        if (!expiry.isAfter(issued)) {
            throw new BusinessRuleViolationException(
                    "Medical expiry must be after issued date");
        }

        long months = ChronoUnit.MONTHS.between(issued, expiry);

        if (months > 6) {
            throw new BusinessRuleViolationException(
                    "Medical validity cannot exceed 6 months");
        }
    }
}