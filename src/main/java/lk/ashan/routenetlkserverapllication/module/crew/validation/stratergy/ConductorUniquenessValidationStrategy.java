package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorUniquenessValidationStrategy implements ConductorValidationStrategy {

    private final ConductorRepository conductorRepository;

    @Override
    public void validateCreate(ConductorValidationContext context) {

        if (conductorRepository.existsByNumber(context.getNumber())) {
            throw new ResourceExistsException("Conductor number already exists");
        }

    }

    @Override
    public void validateUpdate(ConductorValidationContext context) {

        if (conductorRepository.existsByNumberAndIdNot(context.getNumber(), context.getId())) {
            throw new ResourceExistsException("Conductor number already exists");
        }

    }
}
