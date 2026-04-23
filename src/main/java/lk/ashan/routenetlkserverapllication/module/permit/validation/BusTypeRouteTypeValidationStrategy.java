package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
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
public class BusTypeRouteTypeValidationStrategy implements PermitValidationStrategy {

    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;

    @Override
    public void validate(PermitValidationContext context) {

        Vehicle vehicle = vehicleRepository.findById(context.getVehicleId())
                .orElseThrow(() -> new BusinessRuleViolationException(
                        String.format("Vehicle with id %d does not exist", context.getVehicleId())
                ));

        String busType = vehicle.getBustype().getName().toUpperCase();
        String routeType = routeRepository.findById(context.getRouteId())
                .orElseThrow(()->new ResourceNotFoundException("Route not found")).getRoutetype().getName().toLowerCase();

        List<String> allowed = VALID_COMBINATIONS.get(routeType);
        if (allowed == null || !allowed.contains(busType)) {
            throw new BusinessRuleViolationException(
                    String.format("Invalid combination: Type %s buses cannot be used on %s route.", busType, routeType)
            );
        }
    }

    private static final Map<String, List<String>> VALID_COMBINATIONS = Map.of(
            "inter provincial",List.of("AA","A","B+","B"),
            "intra provincial",List.of("AA","A","B+","B","C","D","E")
    );
}
