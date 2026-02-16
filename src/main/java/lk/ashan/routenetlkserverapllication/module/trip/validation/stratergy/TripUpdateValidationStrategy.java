package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;


import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripUpdateContext;

/**
 * Base interface for trip update validation strategies
 */
public interface TripUpdateValidationStrategy {
    void validate(TripUpdateContext context);
}
