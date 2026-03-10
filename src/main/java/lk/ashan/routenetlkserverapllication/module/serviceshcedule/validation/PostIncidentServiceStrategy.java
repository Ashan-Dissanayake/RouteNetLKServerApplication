package lk.ashan.routenetlkserverapllication.module.serviceshcedule.validation;

import org.springframework.stereotype.Component;

@Component
public class PostIncidentServiceStrategy implements VehicleServiceEvaluationStrategy {

    private static final int SERVICE_TYPE_POST_INCIDENT = 3;

    @Override
    public boolean isServiceRequired(VehicleServiceContext context) {
        return context.getIncident() != null;
    }

    @Override
    public Integer getServiceTypeId() {
        return SERVICE_TYPE_POST_INCIDENT;
    }
}
