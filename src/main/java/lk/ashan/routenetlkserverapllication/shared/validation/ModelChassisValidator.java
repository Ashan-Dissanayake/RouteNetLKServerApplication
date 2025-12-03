package lk.ashan.routenetlkserverapllication.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;

public class ModelChassisValidator implements ConstraintValidator<ValidModelChassis, VehicleRequestDto> {

    @Override
    public boolean isValid(VehicleRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getMake() == null || dto.getChasisnumber() == null) return true; // null handled by @NotBlank
        String regex = BusPattern.CHASSIS_REGEX.get(dto.getMake().getName());
        if (regex == null) return true; // unknown model, skip validation
        return dto.getChasisnumber().matches(regex);
    }
}
