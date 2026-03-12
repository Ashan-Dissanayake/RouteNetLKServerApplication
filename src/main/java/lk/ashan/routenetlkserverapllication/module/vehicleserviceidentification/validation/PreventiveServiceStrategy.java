package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(3)
public class PreventiveServiceStrategy implements VehicleServiceEvaluationStrategy {

    private static final int SERVICE_TYPE_PREVENTIVE = 2;
    private static final int KM_THRESHOLD = 16000;

    @Override
    public boolean isServiceRequired(VehicleServiceContext context) {
        Integer currentMileage = context.getOdometer();
        Integer lastServiceMileage = context.getLastServiceMileage();

        if (currentMileage == null) return false;
        if (lastServiceMileage == null) lastServiceMileage = 0;

        // Service only if vehicle crossed threshold since last preventive service
        return (currentMileage - lastServiceMileage) >= KM_THRESHOLD;
    }

    @Override
    public Integer getServiceTypeId() {
        return SERVICE_TYPE_PREVENTIVE;
    }

    @Override
    public LocalDate getSuggestedStartDate(VehicleServiceContext context) {
        return LocalDate.now(); // can start today
    }

    @Override
    public LocalDate getSuggestedEndDate(VehicleServiceContext context) {
        return LocalDate.now().plusDays(7); // 7-day scheduling window
    }
}
