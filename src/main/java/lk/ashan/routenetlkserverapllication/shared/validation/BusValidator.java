package lk.ashan.routenetlkserverapllication.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;

public class BusValidator implements ConstraintValidator<ValidBus, VehicleRequestDto> {

    @Override
    public boolean isValid(VehicleRequestDto bus, ConstraintValidatorContext context) {
        if (bus.getMake() == null || bus.getChasisnumber() == null || bus.getEnginenumber() == null) {
            return true; // @NotBlank handles null/empty separately
        }

        String chassisPattern = BusPattern.CHASSIS_REGEX.get(bus.getMake().getName());
        String enginePattern = BusPattern.ENGINE_REGEX.get(bus.getMake().getName());

        boolean chassisValid = bus.getChasisnumber().matches(chassisPattern);
        boolean engineValid = bus.getEnginenumber().matches(enginePattern);

        if (!chassisValid || !engineValid) {
            context.disableDefaultConstraintViolation();
            if (!chassisValid) {
                context.buildConstraintViolationWithTemplate("Invalid chassis number for " + bus.getMake().getName())
                        .addPropertyNode("chassisNumber").addConstraintViolation();
            }
            if (!engineValid) {
                context.buildConstraintViolationWithTemplate("Invalid engine number for " + bus.getMake().getName())
                        .addPropertyNode("engineNumber").addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}

