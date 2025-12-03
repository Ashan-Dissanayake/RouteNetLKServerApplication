package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.modelchassis;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seed.VehicleValidationData;

public class ModelChassisValidator implements ConstraintValidator<ValidModelChassis, VehicleRequestDto> {

    private ModelChassisValidationStrategy strategy;

    @Override
    public void initialize(ValidModelChassis constraintAnnotation) {
        this.strategy = new MapBasedModelChassisValidator(VehicleValidationData.CHASSIS_REGEX);
    }

    @Override
    public boolean isValid(VehicleRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getMake() == null || dto.getChasisnumber() ==null) return true;
        return strategy.isValid(dto.getMake().getName(),dto.getChasisnumber());

    }
}


//null handled by @NotBlank
//if (dto.getMake() == null || dto.getChasisnumber() == null) return true;
//String regex = VehicleValidationData.CHASSIS_REGEX.get(dto.getMake().getName());
//if (regex == null) return true; // unknown model, skip validation
//return dto.getChasisnumber().matches(regex);
