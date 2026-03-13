package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleUniquenessValidationStrategy implements VehicleValidationStrategy {

    private final VehicleRepository vehicleRepository;

    @Override
    public void validateCreate(VehicleCreateRequestDto request) {
        if (vehicleRepository.existsByNumber(request.getNumber())) {
            throw new ResourceExistsException("Vehicle number already exists.");
        }
    }

    @Override
    public void validateUpdate(VehicleUpdateRequestDto request) {
         // Add uniqueness checks for update if necessary (often involves checking duplication against OTHER ids)
         // Current VehicleService didn't strictly have update uniqueness checks, but good to add if needed.
         // Leaving empty as per original code logic usually, but let's check original.
         // Original code didn't have validateVehicleUniquenessForUpdate.
    }
}
