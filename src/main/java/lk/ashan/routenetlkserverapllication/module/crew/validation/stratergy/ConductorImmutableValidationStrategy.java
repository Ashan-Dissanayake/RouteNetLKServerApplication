package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validation strategy to ensure immutability of the Conductor entity.
 * This strategy prevents changes to certain fields during updates.
 */
@Component
@RequiredArgsConstructor
public class ConductorImmutableValidationStrategy implements ConductorValidationStrategy {

    private final ConductorRepository conductorRepository;

    /**
     * Validates the creation of a Conductor entity.
     *
     * @param context the validation context containing the data for validation
     */
    @Override
    public void validateCreate(ConductorValidationContext context) {}

    /**
     * Validates the update of a Conductor entity.
     * Ensures that the employee associated with the Conductor cannot be changed.
     *
     * @param context the validation context containing the data for validation
     * @throws ResourceNotFoundException if the Conductor entity is not found
     * @throws BusinessRuleViolationException if an attempt is made to change the associated employee
     */
    @Override
    public void validateUpdate(ConductorValidationContext context) {
        Conductor existing = conductorRepository.findById(context.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Conductor not found"));

        if (!existing.getEmployee().getId().equals(context.getEmployeeId())) {
            throw new BusinessRuleViolationException("Employee cannot be changed");
        }
    }
}
