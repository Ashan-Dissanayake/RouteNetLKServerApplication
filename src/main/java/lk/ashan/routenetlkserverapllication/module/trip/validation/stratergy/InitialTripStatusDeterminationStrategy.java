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
 * Updated to use centralized VehicleConflictDetectionStrategy
 * ISSUE #2: Now leverages conflict detection strategy
 */
@Component
@RequiredArgsConstructor
public class InitialTripStatusDeterminationStrategy {

    private final TripStatusRepository tripStatusRepository;
    private final VehicleConflictDetectionStrategy conflictDetectionStrategy;

    /**
     * Determines the appropriate initial status for a trip based on permit vehicle availability
     */
    public Tripstatus determineInitialStatus(
            Permite permit,
            LocalDate serviceDate,
            LocalTime departure,
            LocalTime arrival) {

        Vehicle permitVehicle = permit.getVehicle();

        // Use centralized conflict detection to check vehicle availability
        boolean isAvailable = conflictDetectionStrategy.isVehicleAvailable(
                permitVehicle,
                serviceDate,
                departure,
                arrival,
                -1  // No current trip ID for new trip
        );

        if (isAvailable) {
            return getReadyStatus();
        } else {
            return getNeedsVehicleOverrideStatus();
        }
    }

    private Tripstatus getReadyStatus() {
        return tripStatusRepository.findByName("READY")
                .orElseThrow(() -> new ResourceNotFoundException("READY status not found"));
    }

    private Tripstatus getNeedsVehicleOverrideStatus() {
        return tripStatusRepository.findByName("NEEDS VEHICLE OVERRIDE")
                .orElseThrow(() -> new ResourceNotFoundException("NEEDS VEHICLE OVERRIDE status not found"));
    }
}
