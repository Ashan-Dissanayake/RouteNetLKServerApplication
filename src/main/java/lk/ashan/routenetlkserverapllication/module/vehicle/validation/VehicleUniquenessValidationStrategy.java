package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;
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
        if (vehicleRepository.existsByCode(request.getCode())) {
            throw new ResourceExistsException("Vehicle code already exists.");
        }
        if (vehicleRepository.existsByNumber(request.getNumber())) {
            throw new ResourceExistsException("Vehicle number already exists.");
        }
        if (vehicleRepository.existsByChasisnumber(request.getChasisnumber())) {
            throw new ResourceExistsException("Vehicle chassis number already exists.");
        }
        if (vehicleRepository.existsByEnginenumber(request.getEnginenumber())) {
            throw new ResourceExistsException("Vehicle engine number already exists.");
        }
        if (vehicleRepository.existsByCodeOrChasisnumber(request.getCode(), request.getChasisnumber())) {
            throw new ResourceExistsException("Code cannot reference a chassis number already used by another vehicle");
        }
        if (vehicleRepository.existsByCodeOrEnginenumber(request.getCode(), request.getEnginenumber())) {
            throw new ResourceExistsException("Code cannot reference an engine number already used by another vehicle");
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
