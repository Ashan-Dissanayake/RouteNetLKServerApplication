package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.permit.repository.ServiceTypeRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BusTypeServiceTypeValidationStrategy implements PermitValidationStrategy {

    private final VehicleRepository vehicleRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    @Override
    public void validate(PermitValidationContext context) {
        Vehicle vehicle = vehicleRepository.findById(context.getVehicleId())
                .orElseThrow(() -> new BusinessRuleViolationException(
                        String.format("Vehicle with id %d does not exist", context.getVehicleId())
                ));

        String busType = vehicle.getBustype().getName().toUpperCase();
        String serviceType = serviceTypeRepository.findById(context.getServiceTypeId())
                .orElseThrow(()->new ResourceNotFoundException("Service type not found")).getName().toLowerCase();

        List<String> allowed = VALID_COMBINATIONS.get(serviceType);
        if (allowed == null || !allowed.contains(busType)) {
            throw new BusinessRuleViolationException(
                    String.format("Invalid combination: %s cannot be used for %s service.", busType, serviceType)
            );
        }
    }

    private static final Map<String, List<String>> VALID_COMBINATIONS = Map.of(
            "luxury",List.of("AA"),
            "super luxury",List.of("AA"),
            "normal",List.of("A","A+","B","B+","C","D","E"),
            "semi luxury",List.of("A","A+","B","B+")
    );

}
