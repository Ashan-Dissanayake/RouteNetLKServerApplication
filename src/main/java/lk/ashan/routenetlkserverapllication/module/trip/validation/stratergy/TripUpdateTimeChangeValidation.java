package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;


import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

/**
 * Validates time changes during update
 * Re-evaluates minimum gap rule when departure time changes
 * ISSUE #5 & #10: Consistent validation with proper self-exclusion
 */
@Component
public class TripUpdateTimeChangeValidation implements TripUpdateValidationStrategy {
    
    @Override
    public void validate(TripUpdateContext context) {
        
        // If time hasn't changed, no validation needed
        if (!context.isTimeChanged()) {
            return;
        }
        
        // Validate minimum gap with other trips on same route/terminal/date
        validateMinimumGap(context);
        
        // Validate departure before arrival
        if (!context.getNewDeparture().isBefore(context.getNewArrival())) {
            throw new BusinessRuleViolationException(
                "Departure time must be before arrival time"
            );
        }
    }
    
    /**
     * ISSUE #10: Validates minimum gap properly excluding the trip being updated
     */
    private void validateMinimumGap(TripUpdateContext context) {
        
        Integer currentTripId = context.getExistingTrip().getId();
        Integer minGapMinutes = context.getMinGapMinutes();
        
        for (Trip existingTrip : context.getPermitRouteOriginExTrips()) {
            
            // ISSUE #10: Exclude the trip being updated from comparison
            if (existingTrip.getId().equals(currentTripId)) {
                continue;
            }
            
            // Use midnight-aware comparison
            if (!MidnightAwareTimeComparator.satisfiesMinimumGap(
                    existingTrip.getTodepature(),
                    context.getNewDeparture(),
                    minGapMinutes)) {
                throw new BusinessRuleViolationException(
                    "Updated departure time violates minimum gap rule. " +
                    "Required gap: " + minGapMinutes + " minutes. " +
                    "Conflict with trip departing at: " + existingTrip.getTodepature()
                );
            }
        }
    }
}
