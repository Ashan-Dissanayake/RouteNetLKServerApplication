package lk.ashan.routenetlkserverapllication.module.serviceshcedule.validation;

public interface VehicleServiceEvaluationStrategy {

    boolean isServiceRequired(VehicleServiceContext context);

    Integer getServiceTypeId();

}
