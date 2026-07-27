package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validation strategy to ensure the uniqueness of driver details such as license number and driver number.
 * This class implements the {@link DriverValidationStrategy} interface.
 */
@Component
@RequiredArgsConstructor
public class DriverUniquenessValidationStrategy implements DriverValidationStrategy {

    private final DriverRepository driverRepository;

    /**
     * Validates the creation of a driver by checking the uniqueness of the license number and driver number.
     *
     * @param context the validation context containing driver details
     * @throws ResourceExistsException if the license number or driver number already exists
     */
    @Override
    public void validateCreate(DriverValidationContext context) {

        if (driverRepository.existsByLicensenumber(context.getLicenseNumber())) {
            throw new ResourceExistsException("License number already exists");
        }

        if (driverRepository.existsByEmployeeId(context.getEmployeeId())) {
            throw new ResourceExistsException("A driver profile already exists for this employee");
        }

    }

    /**
     * Validates the update of a driver by ensuring the uniqueness of the license number and driver number,
     * excluding the current driver being updated.
     *
     * @param context the validation context containing driver details
     * @throws ResourceExistsException if the license number or driver number already exists for another driver
     */
    @Override
    public void validateUpdate(DriverValidationContext context) {

        if (driverRepository.existsByLicensenumberAndIdNot(context.getLicenseNumber(), context.getId())) {
            throw new ResourceExistsException("License number already exists");
        }

        // Employee uniqueness check for update (excluding current driver id)
        if (driverRepository.existsByEmployeeIdAndIdNot(context.getEmployeeId(), context.getId())) {
            throw new ResourceExistsException("A driver profile already exists for this employee");
        }

    }
}
