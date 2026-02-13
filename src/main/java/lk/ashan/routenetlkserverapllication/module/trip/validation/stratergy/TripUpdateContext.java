package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Context for trip update validations
 * Reuses existing validation strategies where possible
 */
@Getter
@Builder
public class TripUpdateContext {
    
    // The trip being updated
    private final Trip existingTrip;
    
    // Updated values
    private final LocalDate newServiceDate;
    private final LocalTime newDeparture;
    private final LocalTime newArrival;
    private final Integer newOriginTerminalId;
    private final Integer newPermitId;
    
    // For validation - similar to TripValidationContext
    private final List<Trip> permitRouteOriginExTrips;
    private final Integer minGapMinutes;
    private final Permite newPermit;
    
    // Flags for what changed
    private final boolean permitChanged;
    private final boolean timeChanged;
    private final boolean dateChanged;
    private final boolean terminalChanged;
}
