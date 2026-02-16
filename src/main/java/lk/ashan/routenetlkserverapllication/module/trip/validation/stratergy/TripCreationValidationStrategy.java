package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;


import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripCreateContext;

public interface TripCreationValidationStrategy {
    void validate(TripCreateContext context);
}
