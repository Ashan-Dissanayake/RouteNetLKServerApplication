package lk.ashan.routenetlkserverapllication.module.serviceshcedule.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
