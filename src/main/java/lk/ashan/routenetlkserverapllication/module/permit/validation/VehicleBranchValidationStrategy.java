package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.InvalidVehicleBranchException;
import org.springframework.stereotype.Component;

@Component
public class VehicleBranchValidationStrategy implements PermitValidationStrategy{
    @Override
    public void validate(PermitValidationContext context) {
        if (context.getVehicleBranchId() == null ||
                context.getRequestBranchId() == null) {
            return;
        }

        if (!context.getVehicleBranchId()
                .equals(context.getRequestBranchId())) {

            throw new InvalidVehicleBranchException(
                    "Vehicle is not in corresponding branch"
            );
        }
    }
}
