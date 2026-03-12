package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(2)
public class PreTripServiceStrategy implements VehicleServiceEvaluationStrategy {

    private static final int SERVICE_TYPE_PRE_TRIP = 1;

    @Override
    public boolean isServiceRequired(VehicleServiceContext context) {
        // PreTrip required flag already computed in context
        return context.isPreTripRequired();
    }

    @Override
    public Integer getServiceTypeId() {
        return SERVICE_TYPE_PRE_TRIP;
    }

    @Override
    public LocalDate getSuggestedStartDate(VehicleServiceContext context) {
        return LocalDate.now(); // schedule today
    }

    @Override
    public LocalDate getSuggestedEndDate(VehicleServiceContext context) {
        return LocalDate.now().plusDays(1); // small window for daily pre-trip
    }
}
