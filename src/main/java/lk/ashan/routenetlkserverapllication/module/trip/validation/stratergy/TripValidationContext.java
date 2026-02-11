package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class TripValidationContext {
    private final List<Trip> permitRouteOriginExTrips;
    private final List<Trip> permitDoServiceExTrips;
    private final Integer minGapMinutes;
    private final LocalTime requestedDeparture;

    private final int tripNo;
}
