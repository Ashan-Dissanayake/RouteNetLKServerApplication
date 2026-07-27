package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

/**
 * Interface for defining a strategy to validate trip-related operations.
 */
public interface TripValidationStrategy {

    /**
     * Validates the context for creating a trip.
     *
     * @param context the context containing the data to be validated
     * @throws IllegalArgumentException if the validation fails
     */
    void validateCreate(TripValidationContext context);
}
