package lk.ashan.routenetlkserverapllication.module.serviceshcedule.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class PreTripServiceStrategy implements VehicleServiceEvaluationStrategy {

    private static final int SERVICE_TYPE_PRE_TRIP = 1;

    @Override
    public boolean isServiceRequired(VehicleServiceContext context) {

        if (!context.isPreTripRequired()) {
            return false;
        }

        if (context.getVehicle() == null) {
            return false;
        }

        if (!context.getVehicle().getVehiclestatus().getName().equals("Active")) {
            return false;
        }

        return true;
    }

    @Override
    public Integer getServiceTypeId() {
        return SERVICE_TYPE_PRE_TRIP;
    }
}
