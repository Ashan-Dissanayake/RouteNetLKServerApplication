package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripCreateContext;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class TripMinGapCreationValidationStrategy implements TripCreationValidationStrategy {

    @Override
    public void validate(TripCreateContext context) {

        for (Trip existing : context.getPermitRouteOriginExTrips()) {

            long diffMinutes = Math.abs(
                    ChronoUnit.MINUTES.between(
                            existing.getTodepature(),
                            context.getRequestedDeparture()
                    )
            );

            if (diffMinutes < context.getMinGapMinutes()) {
                throw new BusinessRuleViolationException(
                        "Trip cannot be created: another trip departs from the same terminal within "
                                + context.getMinGapMinutes() + " minutes."
                );
            }
        }
    }
}
