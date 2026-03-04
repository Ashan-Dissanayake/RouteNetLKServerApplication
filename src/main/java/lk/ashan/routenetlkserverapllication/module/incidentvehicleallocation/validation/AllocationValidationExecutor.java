package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AllocationValidationExecutor {

    private final List<AllocationValidationStrategy> strategies;

    public void validate(AllocationContext context) {
        for (AllocationValidationStrategy strategy : strategies) {
            strategy.validate(context);
        }
    }
}
