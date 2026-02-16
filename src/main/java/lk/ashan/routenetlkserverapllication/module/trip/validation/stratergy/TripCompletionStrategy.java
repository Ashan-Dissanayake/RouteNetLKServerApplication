package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class TripCompletionStrategy {
    
    private final TripStatusRepository tripStatusRepository;
    private final TripExecutionStrategy tripExecutionStrategy;

    public void completeTrip(Trip trip, LocalTime actualArrivalTime) {
        
        // Validate current status - only IN_PROGRESS trips can be completed
        String currentStatus = trip.getTripstatus().getName();
        if (!"IN PROGRESS".equalsIgnoreCase(currentStatus)) {
            throw new BusinessRuleViolationException(
                "Only IN_PROGRESS trips can be completed. Current status: " + currentStatus
            );
        }
        
        // Record actual arrival time if provided
        if (actualArrivalTime != null) {
            trip.setToarrival(actualArrivalTime);
        }
        
        // Get effective vehicle for implicit release
        tripExecutionStrategy.getEffectiveVehicle(trip);
        
        // Update status to COMPLETED
        Tripstatus completedStatus = tripStatusRepository.findByName("Completed")
            .orElseThrow(() -> new ResourceNotFoundException("COMPLETED status not found"));
        
        trip.setTripstatus(completedStatus);

    }
    
    /**
     * Complete trip without updating arrival time
     */
    public void completeTrip(Trip trip) {
        completeTrip(trip, null);
    }

}
