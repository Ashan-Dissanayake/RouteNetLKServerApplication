package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BusTypeServiceTypeValidationStrategy implements PermitValidationStrategy {

    @Override
    public void validate(PermitValidationContext context) {
        String busType = context.getBusType().getName().toUpperCase();
        String serviceType = context.getServiceType().getName().toLowerCase();

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
