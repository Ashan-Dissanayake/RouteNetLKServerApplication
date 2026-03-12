package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import java.time.LocalDate;

public interface VehicleServiceEvaluationStrategy {

    /**
     * Determine if a service is required for this vehicle/context.
     */
    boolean isServiceRequired(VehicleServiceContext context);

    /**
     * Return the service type ID that this strategy handles.
     */
    Integer getServiceTypeId();

    /**
     * Suggested start date for scheduling this service.
     */
    default LocalDate getSuggestedStartDate(VehicleServiceContext context) {
        return LocalDate.now();
    }

    /**
     * Suggested end date for scheduling this service.
     */
    default LocalDate getSuggestedEndDate(VehicleServiceContext context) {
        return LocalDate.now().plusDays(5);
    }

    /**
     * For preventive services: return last mileage when this service type was done.
     */
    default Integer getLastServiceMileage(VehicleServiceContext context) {
        return null; // default null, override if needed
    }
}
