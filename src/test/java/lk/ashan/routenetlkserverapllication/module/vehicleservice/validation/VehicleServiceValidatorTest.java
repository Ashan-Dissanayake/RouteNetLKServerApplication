package lk.ashan.routenetlkserverapllication.module.vehicleservice.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePartDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceValidatorTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private VehicleServiceValidator vehicleServiceValidator;

    // =========================================================
    // VALIDATE CREATE - PART QUANTITIES
    // =========================================================

    @Test
    void validateCreate_ShouldPass_WhenPartListIsNull() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(null)
                        .build();

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenPartListIsEmpty() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(Collections.emptyList())
                        .build();

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenPartQuantityIsPositive() {

        VehicleServicePartDto part =
                partWithQuantity(new BigDecimal("2"));

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(List.of(part))
                        .build();

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenPartQuantityIsDecimalPositive() {

        VehicleServicePartDto part =
                partWithQuantity(new BigDecimal("0.50"));

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(List.of(part))
                        .build();

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenPartQuantityIsZero() {

        VehicleServicePartDto part =
                partWithQuantity(BigDecimal.ZERO);

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(List.of(part))
                        .build();

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(
                incidentRepository,
                vehicleRepository
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenPartQuantityIsNegative() {

        VehicleServicePartDto part =
                partWithQuantity(new BigDecimal("-1"));

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(List.of(part))
                        .build();

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(
                incidentRepository,
                vehicleRepository
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenPartQuantityIsNull() {

        VehicleServicePartDto part =
                partWithQuantity(null);

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(List.of(part))
                        .build();

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(
                incidentRepository,
                vehicleRepository
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenAnyPartHasInvalidQuantity() {

        VehicleServicePartDto validPart =
                partWithQuantity(new BigDecimal("2"));

        VehicleServicePartDto invalidPart =
                partWithQuantity(BigDecimal.ZERO);

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleserviceparts(
                                List.of(validPart, invalidPart)
                        )
                        .build();

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(
                incidentRepository,
                vehicleRepository
        );
    }

    // =========================================================
    // VALIDATE CREATE - BREAKDOWN REPAIR INCIDENT
    // =========================================================

    @Test
    void validateCreate_ShouldPass_WhenServiceTypeIsNotBreakdownRepair() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleservicetype(
                                serviceType("PREVENTIVE_MAINTENANCE")
                        )
                        .incident(null)
                        .build();

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );

        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validateCreate_ShouldPass_WhenBreakdownRepairHasValidIncident() {

        VehicleServiceCreateRequestDto request =
                validBreakdownRepairRequest();

        when(incidentRepository.findById(100))
                .thenReturn(Optional.of(
                        Incident.builder()
                                .id(100)
                                .build()
                ));

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );

        verify(incidentRepository)
                .findById(100);

        verify(vehicleRepository)
                .findById(1);
    }

    @Test
    void validateCreate_ShouldPass_WhenBreakdownRepairServiceTypeDiffersOnlyByCase() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleservicetype(
                                serviceType("breakdown_repair")
                        )
                        .incident(
                                incidentReference(100)
                        )
                        .build();

        when(incidentRepository.findById(100))
                .thenReturn(Optional.of(
                        Incident.builder()
                                .id(100)
                                .build()
                ));

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenBreakdownRepairHasNoIncident() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleservicetype(
                                serviceType("BREAKDOWN_REPAIR")
                        )
                        .incident(null)
                        .build();

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "An active incident ID must be attached for breakdown repairs",
                exception.getMessage()
        );

        verifyNoInteractions(
                incidentRepository,
                vehicleRepository
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenBreakdownRepairIncidentIdIsNull() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .vehicleservicetype(
                                serviceType("BREAKDOWN_REPAIR")
                        )
                        .incident(
                                incidentReference(null)
                        )
                        .build();

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "An active incident ID must be attached for breakdown repairs",
                exception.getMessage()
        );

        verifyNoInteractions(
                incidentRepository,
                vehicleRepository
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenAttachedIncidentDoesNotExist() {

        VehicleServiceCreateRequestDto request =
                validBreakdownRepairRequest();

        when(incidentRepository.findById(100))
                .thenReturn(Optional.empty());

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "Attached incident target not found",
                exception.getMessage()
        );

        verify(incidentRepository)
                .findById(100);

        verifyNoInteractions(vehicleRepository);
    }

    // =========================================================
    // VALIDATE CREATE - VEHICLE AVAILABILITY
    // =========================================================

    @Test
    void validateCreate_ShouldPass_WhenVehicleIsAvailable() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .build();

        stubVehicleAsAvailable();

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );

        verify(vehicleRepository)
                .findById(1);
    }

    @Test
    void validateCreate_ShouldThrowException_WhenVehicleDoesNotExist() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .build();

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.empty());

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "Vehicle target not found",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenVehicleIsUnderMaintenance() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .build();

        stubVehicleStatus("UNDER_MAINTENANCE");

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "This vehicle is already booked into a maintenance loop",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenVehicleStatusIsNotUnderMaintenance() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .build();

        stubVehicleStatus("AVAILABLE");

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateCreate(request)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenVehicleStatusDiffersOnlyByCase() {

        VehicleServiceCreateRequestDto request =
                validRequestBuilder()
                        .build();

        stubVehicleStatus("under_maintenance");

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> vehicleServiceValidator.validateCreate(request)
                );

        assertEquals(
                "This vehicle is already booked into a maintenance loop",
                exception.getMessage()
        );
    }

    // =========================================================
    // STATUS TRANSITION - SAME STATUS
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusRemainsSame() {

        assertValidTransition(
                "PENDING",
                "PENDING"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenSameStatusDiffersOnlyByCase() {

        assertValidTransition(
                "pending",
                "PENDING"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenSameStatusContainsWhitespace() {

        assertValidTransition(
                "  PENDING  ",
                " pending "
        );
    }

    // =========================================================
    // STATUS TRANSITION - PENDING
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenPendingChangesToScheduled() {

        assertValidTransition(
                "PENDING",
                "SCHEDULED"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenPendingChangesToInProgress() {

        assertValidTransition(
                "PENDING",
                "IN_PROGRESS"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenPendingChangesToCancelled() {

        assertValidTransition(
                "PENDING",
                "CANCELLED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenPendingChangesToCompleted() {

        assertInvalidTransition(
                "PENDING",
                "COMPLETED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - SCHEDULED
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenScheduledChangesToInProgress() {

        assertValidTransition(
                "SCHEDULED",
                "IN_PROGRESS"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenScheduledChangesToCancelled() {

        assertValidTransition(
                "SCHEDULED",
                "CANCELLED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenScheduledChangesToPending() {

        assertInvalidTransition(
                "SCHEDULED",
                "PENDING"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenScheduledChangesToCompleted() {

        assertInvalidTransition(
                "SCHEDULED",
                "COMPLETED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - IN PROGRESS
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenInProgressChangesToCompleted() {

        assertValidTransition(
                "IN_PROGRESS",
                "COMPLETED"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenInProgressChangesToOnHoldParts() {

        assertValidTransition(
                "IN_PROGRESS",
                "ON_HOLD_PARTS"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenInProgressChangesToCancelled() {

        assertValidTransition(
                "IN_PROGRESS",
                "CANCELLED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenInProgressChangesToScheduled() {

        assertInvalidTransition(
                "IN_PROGRESS",
                "SCHEDULED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - ON HOLD PARTS
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenOnHoldPartsChangesToInProgress() {

        assertValidTransition(
                "ON_HOLD_PARTS",
                "IN_PROGRESS"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenOnHoldPartsChangesToCancelled() {

        assertValidTransition(
                "ON_HOLD_PARTS",
                "CANCELLED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenOnHoldPartsChangesToCompleted() {

        assertInvalidTransition(
                "ON_HOLD_PARTS",
                "COMPLETED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - TERMINAL STATES
    // =========================================================

    @ParameterizedTest
    @ValueSource(strings = {
            "COMPLETED",
            "CANCELLED"
    })
    void validateStatusTransition_ShouldThrowException_WhenTerminalStatusChanges(
            String currentStatus
    ) {

        assertInvalidTransition(
                currentStatus,
                "PENDING"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCompletedChangesToInProgress() {

        assertInvalidTransition(
                "COMPLETED",
                "IN_PROGRESS"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCancelledChangesToScheduled() {

        assertInvalidTransition(
                "CANCELLED",
                "SCHEDULED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - UNKNOWN STATUS
    // =========================================================

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCurrentStatusIsUnknown() {

        InvalidStateTransitionException exception =
                assertThrows(
                        InvalidStateTransitionException.class,
                        () -> vehicleServiceValidator.validateStatusTransition(
                                status("UNKNOWN"),
                                status("PENDING")
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Invalid vehicle service status transition")
        );

        assertTrue(
                exception.getMessage()
                        .contains("UNKNOWN")
        );

        assertTrue(
                exception.getMessage()
                        .contains("PENDING")
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenTargetStatusIsUnknown() {

        InvalidStateTransitionException exception =
                assertThrows(
                        InvalidStateTransitionException.class,
                        () -> vehicleServiceValidator.validateStatusTransition(
                                status("PENDING"),
                                status("UNKNOWN")
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("UNKNOWN")
        );
    }

    // =========================================================
    // STATUS TRANSITION - NORMALIZATION
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenValidTransitionUsesDifferentCase() {

        assertValidTransition(
                "pending",
                "scheduled"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusUsesSpacesInsteadOfUnderscores() {

        assertValidTransition(
                "IN PROGRESS",
                "COMPLETED"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenTargetStatusUsesSpacesInsteadOfUnderscores() {

        assertValidTransition(
                "IN_PROGRESS",
                "ON HOLD PARTS"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenInvalidTransitionUsesDifferentCase() {

        assertInvalidTransition(
                "pending",
                "completed"
        );
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private VehicleServiceCreateRequestDto.VehicleServiceCreateRequestDtoBuilder
    validRequestBuilder() {

        return VehicleServiceCreateRequestDto.builder()
                .vehicle(
                        VehicleSummaryDto.builder()
                                .id(1)
                                .build()
                )
                .vehicleservicetype(
                        serviceType("PREVENTIVE_MAINTENANCE")
                )
                .incident(null)
                .vehicleserviceparts(
                        Collections.emptyList()
                );
    }

    private VehicleServiceCreateRequestDto validBreakdownRepairRequest() {

        return validRequestBuilder()
                .vehicleservicetype(
                        serviceType("BREAKDOWN_REPAIR")
                )
                .incident(
                        incidentReference(100)
                )
                .build();
    }

    private VehicleServicePartDto partWithQuantity(
            BigDecimal quantity
    ) {

        return VehicleServicePartDto.builder()
                .quantity(quantity)
                .build();
    }

    private VehicleServiceTypeDto serviceType(
            String name
    ) {

        return VehicleServiceTypeDto.builder()
                .name(name)
                .build();
    }

    private IncidentSummaryDto incidentReference(
            Integer id
    ) {

        return IncidentSummaryDto.builder()
                .id(id)
                .build();
    }

    private void stubVehicleAsAvailable() {

        stubVehicleStatus("AVAILABLE");
    }

    private void stubVehicleStatus(String status) {

        Vehicle vehicle =
                Vehicle.builder()
                        .id(1)
                        .vehiclestatus(
                                VehicleStatus.builder()
                                        .name(status)
                                        .build()
                        )
                        .build();

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.of(vehicle));
    }

    private VehicleServiceStatus status(String name) {

        return VehicleServiceStatus.builder()
                .name(name)
                .build();
    }

    private void assertValidTransition(
            String currentStatus,
            String targetStatus
    ) {

        assertDoesNotThrow(() ->
                vehicleServiceValidator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    private void assertInvalidTransition(
            String currentStatus,
            String targetStatus
    ) {

        InvalidStateTransitionException exception =
                assertThrows(
                        InvalidStateTransitionException.class,
                        () ->
                                vehicleServiceValidator.validateStatusTransition(
                                        status(currentStatus),
                                        status(targetStatus)
                                )
                );

        assertTrue(
                exception.getMessage()
                        .contains(
                                "Invalid vehicle service status transition"
                        )
        );

        assertTrue(
                exception.getMessage()
                        .contains(
                                currentStatus
                                        .trim()
                                        .toUpperCase()
                                        .replace(" ", "_")
                        )
        );

        assertTrue(
                exception.getMessage()
                        .contains(
                                targetStatus
                                        .trim()
                                        .toUpperCase()
                                        .replace(" ", "_")
                        )
        );
    }
}