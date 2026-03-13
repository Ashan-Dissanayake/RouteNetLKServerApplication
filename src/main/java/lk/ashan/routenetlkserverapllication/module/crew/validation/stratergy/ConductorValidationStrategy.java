package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

public interface ConductorValidationStrategy {
    void validateCreate(ConductorValidationContext context);
    void validateUpdate(ConductorValidationContext context);
}
