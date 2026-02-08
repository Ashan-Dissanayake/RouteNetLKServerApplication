package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.InvalidBusRouteTypeException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidDepartmentDesignationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BusTypeRouteTypeValidationStrategy implements PermitValidationStrategy {

    @Override
    public void validate(PermitValidationContext context) {
        String busType = context.getBusType().getName().toUpperCase();
        String routeType = context.getRouteType().getName().toLowerCase();

        List<String> allowed = VALID_COMBINATIONS.get(routeType);
        if (allowed == null || !allowed.contains(busType)) {
            throw new InvalidBusRouteTypeException(
                    String.format("Invalid combination: Type %s buses cannot be used on %s route.", busType, routeType)
            );
        }
    }

    private static final Map<String, List<String>> VALID_COMBINATIONS = Map.of(
            "inter provincial",List.of("AA","A","B+","B"),
            "intra provincial",List.of("AA","A","B+","B","C","D","E")
    );
}
