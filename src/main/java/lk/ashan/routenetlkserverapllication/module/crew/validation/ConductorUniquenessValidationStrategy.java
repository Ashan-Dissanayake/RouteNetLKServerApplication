package lk.ashan.routenetlkserverapllication.module.crew.validation;

import jakarta.validation.ValidationException;
import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorUniquenessValidationStrategy implements ConductorValidationStrategy {

    private final ConductorRepository conductorRepository;

    @Override
    public void validateCreate(ConductorCreateRequestDto request) {
        if (conductorRepository.existsByNumber(request.getNumber())) {
            throw new ValidationException("Conductor number already exists");
        }
    }

    @Override
    public void validateUpdate(ConductorUpdateRequestDto request) {
        if (conductorRepository.existsByNumberAndIdNot(request.getNumber(), request.getId())) {
            throw new ResourceExistsException("Conductor number already exists");
        }
    }
}
