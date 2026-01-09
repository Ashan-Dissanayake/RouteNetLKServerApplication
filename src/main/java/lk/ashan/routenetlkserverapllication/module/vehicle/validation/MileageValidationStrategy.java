package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidMileageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MileageValidationStrategy implements VehicleValidationStrategy {

    private final VehicleRepository vehicleRepository;

    @Override
    public void validateCreate(VehicleCreateRequestDto request) {
        // Mileage validation usually only relevant for updates or if initial mileage issues
    }

    @Override
    public void validateUpdate(VehicleUpdateRequestDto request) {
        Integer currentMileage = vehicleRepository.findByMyId(request.getId()).getMileage();
        if (request.getMileage() < currentMileage) {
             throw new InvalidMileageException("Mileage cannot be less than current value.");
        }
    }
}
