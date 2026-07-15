package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;


/**
 * Interface for defining validation strategies for Driver operations.
 */
public interface DriverValidationStrategy {

    /**
     * Validates the context for creating a Driver.
     *
     * @param context the validation context containing data for the create operation
     * @throws IllegalArgumentException if validation fails
     */
    void validateCreate(DriverValidationContext context);

    /**
     * Validates the context for updating a Driver.
     *
     * @param context the validation context containing data for the update operation
     * @throws IllegalArgumentException if validation fails
     */
    void validateUpdate(DriverValidationContext context);
}
