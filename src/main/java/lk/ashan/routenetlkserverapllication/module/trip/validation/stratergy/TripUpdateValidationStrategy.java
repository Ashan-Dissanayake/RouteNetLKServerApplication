package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;


/**
 * Base interface for trip update validation strategies
 */
public interface TripUpdateValidationStrategy {
    void validate(TripUpdateContext context);
}
