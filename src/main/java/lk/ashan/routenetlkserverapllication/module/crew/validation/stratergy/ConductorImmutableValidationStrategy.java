package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorImmutableValidationStrategy
        implements ConductorValidationStrategy {

    private final ConductorRepository conductorRepository;

    @Override
    public void validateCreate(ConductorValidationContext context) {}

    @Override
    public void validateUpdate(ConductorValidationContext context) {
        Conductor existing = conductorRepository.findById(context.getId()).
                orElseThrow(()->new ResourceNotFoundException("Conductor not found"));

        if (!existing.getEmployee().getId().equals(context.getEmployeeId())) {
            throw new BusinessRuleViolationException("Employee cannot be changed");
        }

    }

}
