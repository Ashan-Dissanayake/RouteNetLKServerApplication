package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class TripCreateContext {
    private final List<Trip> permitRouteOriginExTrips;
    private final List<Trip> permitDoServiceExTrips;
    private final Integer minGapMinutes;
    private final LocalTime requestedDeparture;

    private final Permite permit;
    private final LocalDate serviceDate;
}
