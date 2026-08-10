package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleBranchValidationStrategy implements PermitValidationStrategy{

    private final VehicleRepository vehicleRepository;

    @Override
    public void validate(PermitValidationContext context) {

       Vehicle vehicle = vehicleRepository.findById(context.getVehicleId())
                .orElseThrow(() -> new BusinessRuleViolationException(
                        String.format("Vehicle with id %d does not exist", context.getVehicleId())
                ));

        if (vehicle.getBranch() == null || context.getRequestBranchId() == null) {
            return;
        }

        if (!vehicle.getBranch().getId().equals(context.getRequestBranchId())) {
            throw new BusinessRuleViolationException(
                    "Vehicle is not in corresponding branch"
            );
        }
    }
}
