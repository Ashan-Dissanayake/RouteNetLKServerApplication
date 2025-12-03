package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seatingcapacity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seed.VehicleValidationData;

public class ModelSeatingValidator implements ConstraintValidator<ValidModelSeating, VehicleRequestDto> {

    private SeatingValidationStrategy strategy;

    @Override
    public void initialize(ValidModelSeating constraintAnnotation) {
        this.strategy = new MapBasedSeatingValidator(VehicleValidationData.MODEL_SEATING_MAP);
    }

    @Override
    public boolean isValid(VehicleRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getMake() == null || dto.getSeatingcapacity() == null) return true;
        return strategy.isValid(dto.getMake().getName(), dto.getSeatingcapacity().getAmount());
    }
}
