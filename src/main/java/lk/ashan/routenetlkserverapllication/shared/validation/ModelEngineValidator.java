package lk.ashan.routenetlkserverapllication.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;

public class ModelEngineValidator implements ConstraintValidator<ValidModelEngine, VehicleRequestDto> {

    @Override
    public boolean isValid(VehicleRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getMake() == null || dto.getEnginenumber() == null) return true;
        String regex = BusPattern.ENGINE_REGEX.get(dto.getMake().getName());
        if (regex == null) return true;
        return dto.getEnginenumber().matches(regex);
    }
}
