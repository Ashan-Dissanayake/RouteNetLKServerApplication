package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import org.springframework.stereotype.Component;

@Component
public class PreventiveServiceStrategy implements VehicleServiceEvaluationStrategy {

    private static final int SERVICE_TYPE_PREVENTIVE = 2;
    private static final int KM_THRESHOLD = 16000;

    @Override
    public boolean isServiceRequired(VehicleServiceContext context) {

        if (context.getOdometer() == null) {
            return false;
        }

        return context.getOdometer() >= KM_THRESHOLD;
    }

    @Override
    public Integer getServiceTypeId() {
        return SERVICE_TYPE_PREVENTIVE;
    }
}
