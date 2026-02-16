package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Determines initial trip status - always PLANNED
 * Trips can only become READY after crew assignment (not part of creation)
 *
 * LOGICAL CORRECTION: Vehicle availability alone is not enough for READY status.
 * A trip needs BOTH vehicle AND crew (driver + conductor) to be READY for execution.
 *
 * Initial creation workflow:
 * 1. Create trip → PLANNED (always)
 * 2. If vehicle unavailable → remains PLANNED (note: needs override)
 * 3. Crew assignment (separate process) → PLANNED → READY
 */
@Component
@RequiredArgsConstructor
public class InitialTripStatusDeterminationStrategy {

    private final TripStatusRepository tripStatusRepository;
    private final VehicleConflictDetectionStrategy conflictDetectionStrategy;

    /**
     * Determines initial status:
     * - PLANNED: Vehicle is available (but still needs crew assignment)
     * - NEEDS VEHICLE OVERRIDE: Vehicle unavailable (maintenance/breakdown/conflict)
     */
    public Tripstatus determineInitialStatus(
            Permite permit,
            LocalDate serviceDate,
            LocalTime departure,
            LocalTime arrival) {

        Vehicle permitVehicle = permit.getVehicle();

        // Check if permit vehicle is available
        boolean isAvailable = conflictDetectionStrategy.isVehicleAvailable(
                permitVehicle,
                serviceDate,
                departure,
                arrival,
                -1  // No current trip ID for new trip
        );

        if (isAvailable) {
            return getPlannedStatus();  // Vehicle OK, needs crew later
        } else {
            return getNeedsVehicleOverrideStatus();  // Vehicle problem
        }
    }

    private Tripstatus getPlannedStatus() {
        return tripStatusRepository.findByName("Planned")
                .orElseThrow(() -> new ResourceNotFoundException("PLANNED status not found"));
    }

    private Tripstatus getNeedsVehicleOverrideStatus() {
        return tripStatusRepository.findByName("Need vehicle override")
                .orElseThrow(() -> new ResourceNotFoundException("NEEDS VEHICLE OVERRIDE status not found"));
    }
}
