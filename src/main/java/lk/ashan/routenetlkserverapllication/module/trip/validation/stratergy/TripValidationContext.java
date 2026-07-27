package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

/**
 * Represents the context for validating a trip.
 * This class contains various attributes related to a trip, such as its ID, branch, permit, route,
 * departure and arrival times, origin terminal, trip type, and whether the time has changed.
 * It is annotated with Lombok annotations to generate boilerplate code like getters, setters, and builders.
 */
@Data
@Builder
public class TripValidationContext {
    private Integer id;
    private Integer branchId;
    private Integer permitId;
    private Integer routeId;
    private LocalTime departure;
    private LocalTime arrival;
    private Integer originTerminalId;
    private Integer triptypeId;
    private boolean isTimeChanged;
}
