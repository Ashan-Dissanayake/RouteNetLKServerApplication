package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentVehicleAllocationValidatorTest {

    @Mock
    private IncidentVehicleAllocationRepository allocationRepository;

    @InjectMocks
    private IncidentVehicleAllocationValidator validator;


    // =========================
    // CREATE - INCIDENT STATE
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenIncidentIsClosed() {

        Incident incident = createIncident("Closed");
        Vehicle vehicle = createVehicle("Available");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(incident, vehicle)
        );

        verifyNoInteractions(allocationRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenIncidentIsResolved() {

        Incident incident = createIncident("Resolved");
        Vehicle vehicle = createVehicle("Available");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(incident, vehicle)
        );

        verifyNoInteractions(allocationRepository);
    }

    @Test
    void validateCreate_shouldAllowIncidentWhenStatusIsInProgress() {

        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Available");

        mockNoActiveAllocations();

        assertDoesNotThrow(
                () -> validator.validateCreate(incident, vehicle)
        );
    }

    @Test
    void validateCreate_shouldAllowIncidentWhenStatusIsReported() {

        Incident incident = createIncident("Reported");
        Vehicle vehicle = createVehicle("Available");

        mockNoActiveAllocations();

        assertDoesNotThrow(
                () -> validator.validateCreate(incident, vehicle)
        );
    }


    // =========================
    // CREATE - VEHICLE STATUS
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenVehicleIsNotAvailable() {

        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("In Service");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(incident, vehicle)
        );

        verifyNoInteractions(allocationRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenVehicleIsUnderMaintenance() {

        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Under Maintenance");

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(incident, vehicle)
        );

        verifyNoInteractions(allocationRepository);
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenVehicleIsAlreadyAllocatedToAnotherEmergency() {

        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Available");

        when(
                allocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                vehicle.getId(),
                                List.of("Assigned", "In Progress")
                        )
        ).thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(incident, vehicle)
        );

        verify(
                allocationRepository
        ).existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                vehicle.getId(),
                List.of("Assigned", "In Progress")
        );

        verify(
                allocationRepository,
                never()
        ).existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                anyInt(),
                anyInt(),
                anyList()
        );

        verify(
                allocationRepository,
                never()
        ).existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                anyInt(),
                anyList()
        );
    }

    @Test
    void validateCreate_shouldAllowAvailableVehicleWhenNoActiveVehicleAllocationExists() {

        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Available");

        mockNoActiveAllocations();

        assertDoesNotThrow(
                () -> validator.validateCreate(incident, vehicle)
        );
    }


    // =========================
    // CREATE - DUPLICATE ALLOCATION
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenDuplicateActiveAllocationExists() {

        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Available");

        when(
                allocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                vehicle.getId(),
                                List.of("Assigned", "In Progress")
                        )
        ).thenReturn(false);

        when(
                allocationRepository
                        .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                incident.getId(),
                                vehicle.getId(),
                                List.of("Assigned", "In Progress")
                        )
        ).thenReturn(true);

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(incident, vehicle)
        );

        verify(allocationRepository)
                .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                incident.getId(),
                vehicle.getId(),
                List.of("Assigned", "In Progress")
        );

        verify(allocationRepository,
                never()
        ).existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                anyInt(),
                anyList()
        );
    }

    @Test
    void validateCreate_shouldAllowAllocationWhenNoDuplicateExists() {
        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Available");

        mockNoActiveAllocations();

        assertDoesNotThrow(() -> validator.validateCreate(incident, vehicle));
    }


    // =========================
    // CREATE - INCIDENT ALLOCATION LIMIT
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenIncidentAlreadyHasActiveAllocation() {

        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Available");

        when(allocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                vehicle.getId(),
                                List.of("Assigned", "In Progress")
                        )
        ).thenReturn(false);

        when( allocationRepository
                        .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                incident.getId(),
                                vehicle.getId(),
                                List.of("Assigned", "In Progress")
                        )
        ).thenReturn(false);

        when(allocationRepository
                        .existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                                incident.getId(),
                                List.of("Assigned", "In Progress")
                        )
        ).thenReturn(true);

        assertThrows(BusinessRuleViolationException.class, () -> validator.validateCreate(incident, vehicle));

        verify(allocationRepository)
                .existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                incident.getId(),
                List.of("Assigned", "In Progress")
        );
    }

    @Test
    void validateCreate_shouldAllowAllocationWhenIncidentHasNoActiveAllocation() {
        Incident incident = createIncident("In Progress");
        Vehicle vehicle = createVehicle("Available");
        mockNoActiveAllocations();
        assertDoesNotThrow(() -> validator.validateCreate(incident, vehicle));
    }


    // =========================
    // INITIAL STATUS
    // =========================

    @Test
    void validateInitialStatus_shouldAllowAssignedStatus() {
        IncidentVehicleAllocationStatus status = createAllocationStatus("ASSIGNED");
        assertDoesNotThrow(() -> validator.validateInitialStatus(status));
    }

    @Test
    void validateInitialStatus_shouldIgnoreCase() {
        IncidentVehicleAllocationStatus status = createAllocationStatus("assigned");
        assertDoesNotThrow(() -> validator.validateInitialStatus(status));
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenStatusIsInProgress() {
        IncidentVehicleAllocationStatus status = createAllocationStatus("IN PROGRESS");
        assertThrows(BusinessRuleViolationException.class, () -> validator.validateInitialStatus(status));
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenStatusIsReleased() {
        IncidentVehicleAllocationStatus status = createAllocationStatus("RELEASED");
        assertThrows(BusinessRuleViolationException.class, () -> validator.validateInitialStatus(status));
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenStatusIsCancelled() {
        IncidentVehicleAllocationStatus status = createAllocationStatus("CANCELLED");
        assertThrows(BusinessRuleViolationException.class, () -> validator.validateInitialStatus(status));
    }


    // =========================
    // STATUS TRANSITIONS
    // =========================

    @Test
    void validateStatusTransition_shouldAllowSameStatus() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("ASSIGNED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("ASSIGNED");

        assertDoesNotThrow(() -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldAllowAssignedToInProgress() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("ASSIGNED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("IN PROGRESS");

        assertDoesNotThrow(() -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldAllowAssignedToCancelled() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("ASSIGNED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("CANCELLED");

        assertDoesNotThrow(() -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldAllowInProgressToReleased() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("IN PROGRESS");

        IncidentVehicleAllocationStatus target = createAllocationStatus("RELEASED");

        assertDoesNotThrow(() -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldAllowInProgressToCancelled() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("IN PROGRESS");

        IncidentVehicleAllocationStatus target = createAllocationStatus("CANCELLED");

        assertDoesNotThrow(() -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenAssignedTransitionsDirectlyToReleased() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("ASSIGNED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("RELEASED");

        assertThrows(BusinessRuleViolationException.class, () -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenAssignedTransitionsDirectlyToUnknownStatus() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("ASSIGNED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("UNKNOWN");

        assertThrows(BusinessRuleViolationException.class, () -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenInProgressTransitionsToAssigned() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("IN PROGRESS");

        IncidentVehicleAllocationStatus target = createAllocationStatus("ASSIGNED");

        assertThrows(BusinessRuleViolationException.class, () -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenReleasedTransitionsToAssigned() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("RELEASED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("ASSIGNED");

        assertThrows(BusinessRuleViolationException.class, () -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenReleasedTransitionsToInProgress() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("RELEASED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("IN PROGRESS");

        assertThrows( BusinessRuleViolationException.class,() -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenCancelledTransitionsToAssigned() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("CANCELLED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("ASSIGNED");

        assertThrows(BusinessRuleViolationException.class, () -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenCancelledTransitionsToInProgress() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("CANCELLED");

        IncidentVehicleAllocationStatus target = createAllocationStatus("IN PROGRESS");

        assertThrows(BusinessRuleViolationException.class, () -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldIgnoreCase() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("assigned");

        IncidentVehicleAllocationStatus target = createAllocationStatus("in progress");

        assertDoesNotThrow(() -> validator.validateStatusTransition(current, target));
    }

    @Test
    void validateStatusTransition_shouldIgnoreLeadingAndTrailingSpaces() {

        IncidentVehicleAllocationStatus current = createAllocationStatus("  ASSIGNED  ");

        IncidentVehicleAllocationStatus target = createAllocationStatus("  IN PROGRESS  ");

        assertDoesNotThrow(() -> validator.validateStatusTransition(current, target));
    }


    // =========================
    // TEST HELPERS
    // =========================

    private Incident createIncident(String statusName) {

        IncidentStatus status = new IncidentStatus();
        status.setName(statusName);

        Incident incident = new Incident();
        incident.setId(1);
        incident.setIncidentstatus(status);

        return incident;
    }

    private Vehicle createVehicle(String statusName) {

        VehicleStatus status = new VehicleStatus();
        status.setName(statusName);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(2);
        vehicle.setVehiclestatus(status);

        return vehicle;
    }

    private IncidentVehicleAllocationStatus createAllocationStatus(String statusName) {

        IncidentVehicleAllocationStatus status = new IncidentVehicleAllocationStatus();
        status.setName(statusName);

        return status;
    }

    private void mockNoActiveAllocations() {

        when(allocationRepository.existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                2,
                                List.of("Assigned", "In Progress")))
                .thenReturn(false);

        when(allocationRepository.existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                1,
                                2,
                                List.of("Assigned", "In Progress")))
                .thenReturn(false);

        when(allocationRepository .existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                                1,
                                List.of("Assigned", "In Progress") ))
                .thenReturn(false);
    }
}
