package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Centralized strategy for detecting vehicle conflicts and availability
 * Single source of truth for all vehicle conflict checking logic
 */
@Component
@RequiredArgsConstructor
public class VehicleConflictDetectionStrategy {
    
    private final TripRepository tripRepository;
    
    /**
     * Checks if a vehicle has conflicts for override assignment
     * 
     * @param vehicleId The vehicle to check
     * @param departure Departure time of the trip
     * @param arrival Arrival time of the trip
     * @param currentTripId The trip ID being checked (or -1 for new trips)
     * @return true if conflict exists, false otherwise
     */
    public boolean hasConflictForOverride(
            Integer vehicleId,
            LocalTime departure,
            LocalTime arrival,
            Integer currentTripId) {
        
        return tripRepository.existsVehicleConflictForOverride(
            vehicleId,
            departure,
            arrival,
            currentTripId
        );
    }
    
    /**
     * Checks if a permit's default vehicle has conflicts on a service date
     * 
     * @param vehicle The permit vehicle to check
     * @param serviceDate The service date
     * @param departure Departure time
     * @param arrival Arrival time
     * @param excludeTripId Trip ID to exclude from check (or null)
     * @return true if conflict exists, false otherwise
     */
    public boolean hasConflictForPermitVehicle(
            Vehicle vehicle,
            LocalDate serviceDate,
            LocalTime departure,
            LocalTime arrival,
            Integer excludeTripId) {
        
        // Check vehicle status
        String vehicleStatus = vehicle.getVehiclestatus().getName();
        if (!"AVAILABLE".equalsIgnoreCase(vehicleStatus)) {
            return true; // Vehicle not available = conflict
        }
        
        // Check if vehicle assigned to other active trips
        List<Trip> activeTrips = tripRepository.findByDoserviceAndTripstatus_NameIn(
            serviceDate,
            List.of("Ready", "In progress", "Delayed", "Suspended")
        );
        
        return activeTrips.stream()
            .filter(trip -> !trip.getId().equals(excludeTripId))
            .anyMatch(trip -> {
                // Check if this trip uses the vehicle (via permit or override)
                Integer tripVehicleId = getEffectiveVehicleId(trip);
                
                if (tripVehicleId != null && tripVehicleId.equals(vehicle.getId())) {
                    // Check time overlap
                    return hasTimeOverlap(
                        trip.getTodepature(), 
                        trip.getToarrival(),
                        departure,
                        arrival
                    );
                }
                return false;
            });
    }
    
    /**
     * Checks if vehicle is available for a specific time window
     * Considers both vehicle status and existing assignments
     */
    public boolean isVehicleAvailable(
            Vehicle vehicle,
            LocalDate serviceDate,
            LocalTime departure,
            LocalTime arrival,
            Integer excludeTripId) {
        
        // Check vehicle status
        String vehicleStatus = vehicle.getVehiclestatus().getName();
        if (!"AVAILABLE".equalsIgnoreCase(vehicleStatus)) {
            return false;
        }
        
        // Check for conflicts
        return !hasConflictForPermitVehicle(
            vehicle,
            serviceDate,
            departure,
            arrival,
            excludeTripId
        );
    }
    
    /**
     * Checks if two time windows overlap
     */
    public boolean hasTimeOverlap(
            LocalTime start1, 
            LocalTime end1, 
            LocalTime start2, 
            LocalTime end2) {
        
        // Handle null cases
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        
        // Standard overlap check: start1 < end2 AND start2 < end1
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    /**
     * Gets the effective vehicle ID for a trip (override takes priority)
     */
    private Integer getEffectiveVehicleId(Trip trip) {
        // Check for active override
        if (trip.getTripvehicleoverrides() != null) {
            return trip.getTripvehicleoverrides().stream()
                .filter(override -> "ACTIVE".equalsIgnoreCase(override.getOverridestatus().getName()))
                .findFirst()
                .map(override -> override.getVehicle().getId())
                .orElse(trip.getPermite().getVehicle().getId());
        }
        
        return trip.getPermite().getVehicle() != null 
            ? trip.getPermite().getVehicle().getId() 
            : null;
    }
    
    /**
     * Validates that a vehicle belongs to the same depot as the trip
     */
    public boolean isVehicleInSameDepot(Vehicle vehicle, Integer branchId) {
        return vehicle.getBranch().getId().equals(branchId);
    }
    
    /**
     * Comprehensive validation for vehicle assignment
     * Combines depot check, availability, and conflict detection
     */
    public ConflictCheckResult validateVehicleAssignment(
            Vehicle vehicle,
            Integer branchId,
            LocalDate serviceDate,
            LocalTime departure,
            LocalTime arrival,
            Integer excludeTripId) {
        
        // Check depot match
        if (!isVehicleInSameDepot(vehicle, branchId)) {
            return ConflictCheckResult.depotMismatch();
        }
        
        // Check vehicle status
        String vehicleStatus = vehicle.getVehiclestatus().getName();
        if (!"AVAILABLE".equalsIgnoreCase(vehicleStatus)) {
            return ConflictCheckResult.notAvailable(vehicleStatus);
        }
        
        // Check for time conflicts
        if (hasConflictForPermitVehicle(vehicle, serviceDate, departure, arrival, excludeTripId)) {
            return ConflictCheckResult.hasConflict();
        }
        
        return ConflictCheckResult.valid();
    }
    
    /**
     * Result object for conflict checking
     */
    public static class ConflictCheckResult {
        private final boolean valid;
        private final String reason;
        
        private ConflictCheckResult(boolean valid, String reason) {
            this.valid = valid;
            this.reason = reason;
        }
        
        public static ConflictCheckResult valid() {
            return new ConflictCheckResult(true, null);
        }
        
        public static ConflictCheckResult depotMismatch() {
            return new ConflictCheckResult(false, "Vehicle does not belong to same depot");
        }
        
        public static ConflictCheckResult notAvailable(String status) {
            return new ConflictCheckResult(false, "Vehicle not available. Status: " + status);
        }
        
        public static ConflictCheckResult hasConflict() {
            return new ConflictCheckResult(false, "Vehicle has overlapping trip assignment");
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getReason() {
            return reason;
        }
    }
}
