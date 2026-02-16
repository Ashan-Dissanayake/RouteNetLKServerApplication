package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.Overridestatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripvehicleoverride;
import lk.ashan.routenetlkserverapllication.module.trip.repository.OverrideStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripVehicleOverrideRepository;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Encapsulates the entire vehicle override approval workflow
 * Ensures atomic validation and persistence to prevent race conditions
 */
@Component
@RequiredArgsConstructor
public class VehicleOverrideApprovalStrategy {
    
    private final VehicleRepository vehicleRepository;
    private final TripVehicleOverrideRepository tripVehicleOverrideRepository;
    private final OverrideStatusRepository overrideStatusRepository;
    private final VehicleConflictDetectionStrategy conflictDetectionStrategy;
    private final TripStateTransitionHandler stateTransitionHandler;
    
    /**
     * Approves a vehicle override with full validation and atomic persistence
     *
     * @param trip      The trip requiring override
     * @param vehicleId The vehicle to assign
     * @throws IllegalStateException if approval fails validation
     */
    @Transactional
    public void approveOverride(Trip trip, Integer vehicleId) {
        
        // Step 1: Validate trip status
        validateTripStatus(trip);
        
        // Step 2: Load and validate vehicle
        Vehicle vehicle = loadAndValidateVehicle(vehicleId);
        
        // Step 3: Validate depot match
        validateDepotMatch(trip, vehicle);
        
        // Step 4: Perform comprehensive conflict check (atomic with transaction)
        validateNoConflicts(trip, vehicle);
        
        // Step 5: Create override record with ACTIVE status
        Tripvehicleoverride override = createOverrideRecord(trip, vehicle);
        
        // Step 6: Transition trip to READY status
        transitionToReady(trip);

    }
    
    /**
     * Validates that trip is in correct status for override approval
     */
    private void validateTripStatus(Trip trip) {
        String currentStatus = trip.getTripstatus().getName();
        
        if (!"NEEDS VEHICLE OVERRIDE".equalsIgnoreCase(currentStatus) &&
            !"NEED VEHICLE OVERRIDE".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException(
                "Override approval only allowed for NEEDS_VEHICLE_OVERRIDE trips. " +
                "Current status: " + currentStatus
            );
        }
    }
    
    /**
     * Loads vehicle and validates it exists and is available
     */
    private Vehicle loadAndValidateVehicle(Integer vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));
        
        String vehicleStatus = vehicle.getVehiclestatus().getName();
        if (!"AVAILABLE".equalsIgnoreCase(vehicleStatus)) {
            throw new IllegalStateException(
                "Vehicle is not available. Current status: " + vehicleStatus
            );
        }
        
        return vehicle;
    }
    
    /**
     * Validates vehicle belongs to same depot as trip
     */
    private void validateDepotMatch(Trip trip, Vehicle vehicle) {
        if (!vehicle.getBranch().getId().equals(trip.getBranch().getId())) {
            throw new IllegalStateException(
                "Vehicle must belong to same depot as trip. " +
                "Trip depot: " + trip.getBranch().getId() + 
                ", Vehicle depot: " + vehicle.getBranch().getId()
            );
        }
    }
    
    /**
     * Validates no conflicts exist - uses centralized conflict detection
     * This is executed within the transaction to ensure atomicity
     */
    private void validateNoConflicts(Trip trip, Vehicle vehicle) {
        
        // Use comprehensive validation from conflict detection strategy
        VehicleConflictDetectionStrategy.ConflictCheckResult result = 
            conflictDetectionStrategy.validateVehicleAssignment(
                vehicle,
                trip.getBranch().getId(),
                trip.getDoservice(),
                trip.getTodepature(),
                trip.getToarrival(),
                trip.getId()
            );
        
        if (!result.isValid()) {
            throw new IllegalStateException(
                "Vehicle override approval failed: " + result.getReason()
            );
        }
    }
    
    /**
     * Creates the override record with proper status
     * ISSUE #11: Override status is now properly set to ACTIVE
     */
    private Tripvehicleoverride createOverrideRecord(Trip trip, Vehicle vehicle) {
        
        Overridestatus activeStatus = overrideStatusRepository.findByName("Active")
            .orElseThrow(() -> new ResourceNotFoundException("ACTIVE override status not found"));
        
        Tripvehicleoverride override = new Tripvehicleoverride();
        override.setTrip(trip);
        override.setVehicle(vehicle);
        override.setOverridestatus(activeStatus);  // ISSUE #11: Status properly set
        override.setDooverride(LocalDate.now());
        override.setReason("Vehicle override approved");
        
        return tripVehicleOverrideRepository.save(override);
    }
    
    /**
     * Transitions trip to READY status using state transition handler
     */
    private void transitionToReady(Trip trip) {
        Tripstatus readyStatus = new Tripstatus();
        readyStatus.setName("Ready");
        
        stateTransitionHandler.transitionTo(trip, readyStatus);
    }
    
    /**
     * Checks if override can be approved without actually approving
     */
    public boolean canApproveOverride(Trip trip, Integer vehicleId) {
        try {
            // Validate status
            String currentStatus = trip.getTripstatus().getName();
            if (!"NEEDS VEHICLE OVERRIDE".equalsIgnoreCase(currentStatus) &&
                !"NEED VEHICLE OVERRIDE".equalsIgnoreCase(currentStatus)) {
                return false;
            }
            
            // Load vehicle
            Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
            if (vehicle == null) {
                return false;
            }
            
            // Check availability and conflicts
            VehicleConflictDetectionStrategy.ConflictCheckResult result = 
                conflictDetectionStrategy.validateVehicleAssignment(
                    vehicle,
                    trip.getBranch().getId(),
                    trip.getDoservice(),
                    trip.getTodepature(),
                    trip.getToarrival(),
                    trip.getId()
                );
            
            return result.isValid();
            
        } catch (Exception e) {
            return false;
        }
    }
}
