package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;


public interface DriverValidationStrategy {
    void validateCreate(DriverValidationContext context);
    void validateUpdate(DriverValidationContext context);
}
