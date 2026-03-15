package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import java.time.LocalDate;

public interface VehicleServiceEvaluationStrategy {

    boolean isServiceRequired(VehicleServiceContext context);
    Integer getServiceTypeId();

    default LocalDate getSuggestedStartDate(VehicleServiceContext context) {
        return LocalDate.now();
    }

    default LocalDate getSuggestedEndDate(VehicleServiceContext context) {
        return LocalDate.now().plusDays(5);
    }

    default Integer getLastServiceMileage(VehicleServiceContext context) {
        return null; // default null, override if needed
    }
}
