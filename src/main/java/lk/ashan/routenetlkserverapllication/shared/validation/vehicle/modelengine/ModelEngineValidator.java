package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.modelengine;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seed.VehicleValidationData;

public class ModelEngineValidator implements ConstraintValidator<ValidModelEngine, VehicleRequestDto> {

    private MapBasedModelEngineValidator strategy;

    @Override
    public void initialize(ValidModelEngine constraintAnnotation) {
        this.strategy = new MapBasedModelEngineValidator(VehicleValidationData.ENGINE_REGEX);
    }

    @Override
    public boolean isValid(VehicleRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getMake() == null || dto.getEnginenumber() == null) return true;
        return strategy.isValid(dto.getMake().getName(),dto.getEnginenumber());
    }
}
