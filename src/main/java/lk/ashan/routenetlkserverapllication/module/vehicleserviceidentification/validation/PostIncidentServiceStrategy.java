package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(1)
public class PostIncidentServiceStrategy implements VehicleServiceEvaluationStrategy {

    private static final int SERVICE_TYPE_POST_INCIDENT = 3;

    @Override
    public boolean isServiceRequired(VehicleServiceContext context) {
        return context.getIncidentId() != null;
    }

    @Override
    public Integer getServiceTypeId() {
        return SERVICE_TYPE_POST_INCIDENT;
    }

    @Override
    public LocalDate getSuggestedStartDate(VehicleServiceContext context) {
        // Incident services should be scheduled immediately
        return LocalDate.now();
    }

    @Override
    public LocalDate getSuggestedEndDate(VehicleServiceContext context) {
        return LocalDate.now().plusDays(1); // urgent, 1-day window
    }
}
