package lk.ashan.routenetlkserverapllication.module.trip.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;

public class DepartureArrivalValidator implements ConstraintValidator<ValidTimeRange, Trip> {

    @Override
    public boolean isValid(Trip trip, ConstraintValidatorContext context) {

        if (trip == null) return true;

        if (trip.getTodepature() == null || trip.getToarrival() == null) return true;

        return trip.getTodepature()
                .isBefore(trip.getToarrival());
    }
}
