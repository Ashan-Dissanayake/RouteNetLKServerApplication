package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

/**
 * Interface defining validation strategies for a Conductor entity.
 */
public interface ConductorValidationStrategy {

    /**
     * Validates the creation of a Conductor entity.
     *
     * @param context the validation context containing necessary data for validation
     * @throws IllegalArgumentException if validation fails
     */
    void validateCreate(ConductorValidationContext context);

    /**
     * Validates the update of a Conductor entity.
     *
     * @param context the validation context containing necessary data for validation
     * @throws IllegalArgumentException if validation fails
     */
    void validateUpdate(ConductorValidationContext context);
}
