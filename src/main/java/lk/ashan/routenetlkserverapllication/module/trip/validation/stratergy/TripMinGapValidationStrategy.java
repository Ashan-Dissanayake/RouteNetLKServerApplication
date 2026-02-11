package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import jakarta.validation.ValidationException;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class TripMinGapValidationStrategy implements TripValidationStrategy {

    @Override
    public void validate(TripValidationContext context) {

        for (Trip existing : context.getPermitRouteOriginExTrips()) {

            long diffMinutes = Math.abs(
                    ChronoUnit.MINUTES.between(
                            existing.getTodepature(),
                            context.getRequestedDeparture()
                    )
            );

            if (diffMinutes < context.getMinGapMinutes()) {
                throw new ValidationException(
                        "Trip cannot be created: another trip departs from the same terminal within "
                                + context.getMinGapMinutes() + " minutes."
                );
            }
        }
    }
}
