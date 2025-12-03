package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seed.VehicleValidationData;

public class BusValidator implements ConstraintValidator<ValidBus, VehicleRequestDto> {

    @Override
    public boolean isValid(VehicleRequestDto bus, ConstraintValidatorContext context) {
        if (bus.getMake() == null || bus.getChasisnumber() == null || bus.getEnginenumber() == null) {
            return true; // @NotBlank handles null/empty separately
        }

        String chassisPattern = VehicleValidationData.CHASSIS_REGEX.get(bus.getMake().getName());
        String enginePattern = VehicleValidationData.ENGINE_REGEX.get(bus.getMake().getName());

        boolean chassisValid = bus.getChasisnumber().matches(chassisPattern);
        boolean engineValid = bus.getEnginenumber().matches(enginePattern);

        if (!chassisValid || !engineValid) {
            context.disableDefaultConstraintViolation();
            if (!chassisValid) {
                context.buildConstraintViolationWithTemplate("Invalid chassis number for " + bus.getMake().getName())
                        .addPropertyNode("chasisnumber").addConstraintViolation();
            }
            if (!engineValid) {
                context.buildConstraintViolationWithTemplate("Invalid engine number for " + bus.getMake().getName())
                        .addPropertyNode("enginenumber").addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}

