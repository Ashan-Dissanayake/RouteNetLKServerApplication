package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

public interface VehicleServiceEvaluationStrategy {

    boolean isServiceRequired(VehicleServiceContext context);

    Integer getServiceTypeId();

}
