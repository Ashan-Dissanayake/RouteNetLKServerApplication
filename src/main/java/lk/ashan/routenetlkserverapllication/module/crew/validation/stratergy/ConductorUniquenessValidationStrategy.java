package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.CrewStatusRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.RouteFamiliarityLevelRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validation strategy to ensure the uniqueness of driver details such as license number and driver number.
 * This class implements the {@link DriverValidationStrategy} interface.
 */
@Component
@RequiredArgsConstructor
public class ConductorUniquenessValidationStrategy implements ConductorValidationStrategy {

    private final ConductorRepository conductorRepository;
    /**
     * Validates the creation of a driver by checking the uniqueness of the license number and driver number.
     *
     * @param context the validation context containing driver details
     * @throws ResourceExistsException if the license number or driver number already exists
     */
    @Override
    public void validateCreate(ConductorValidationContext context) {

        if (!context.getCrewStatus().equalsIgnoreCase("Eligible")) {
            throw new BusinessRuleViolationException("New conductor must have status 'ELIGIBLE'");
        }

        if (!context.getRouteFamiliarityLevel().equalsIgnoreCase("Low")) {
            throw new BusinessRuleViolationException("New conductor route familiarity must have 'LOW'");
        }

        if (conductorRepository.existsByEmployeeId(context.getEmployeeId())) {
            throw new ResourceExistsException("A conductor profile already exists for this employee");
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
    public void validateUpdate(ConductorValidationContext context) {

        // Employee uniqueness check for update (excluding current driver id)
        if (conductorRepository.existsByEmployeeIdAndIdNot(context.getEmployeeId(), context.getId())) {
            throw new ResourceExistsException("A conductor profile already exists for this employee");
        }

    }
}
