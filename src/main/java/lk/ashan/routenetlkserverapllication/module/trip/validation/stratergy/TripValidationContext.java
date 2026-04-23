package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class TripValidationContext {
    private Integer id; // Null for create
    private Integer branchId;
    private Integer permitId;
    private LocalTime departure;
    private LocalTime arrival;
    private Integer originTerminalId;
    private Integer minGapMinutes;
    private Integer triptypeId;

    // Loaded data for gap/overlap checks
    private List<Trip> existingTripsAtTerminal;
    private boolean isTimeChanged;
}
