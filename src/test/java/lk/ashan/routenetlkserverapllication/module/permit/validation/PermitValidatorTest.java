package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.RouteType;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.ServiceType;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.BusType;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermitValidatorTest {

    @Mock
    private PermitRepository permitRepository;

    @InjectMocks
    private PermitValidator permitValidator;

    // =========================================================
    // CREATE - ACTIVE PERMIT UNIQUENESS
    // =========================================================

    @Test
    void validateCreate_ShouldThrowException_WhenActivePermitAlreadyExists() {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, "AA");
        Route route = routeWithId(2, "inter provincial");
        ServiceType serviceType = serviceType("luxury");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                ResourceExistsException.class,
                () -> permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );

        verify(permitRepository, times(1))
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1);
    }

    @Test
    void validateCreate_ShouldPass_WhenNoActivePermitExists() {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, "AA");
        Route route = routeWithId(2, "inter provincial");
        ServiceType serviceType = serviceType("luxury");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );

        verify(permitRepository, times(1))
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1);
    }

    // =========================================================
    // CREATE - BUS TYPE & ROUTE TYPE
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "A, inter provincial",
            "B+, inter provincial",
            "B, inter provincial",

            "B+, intra provincial",
            "B, intra provincial",
            "C, intra provincial",
            "D, intra provincial",
            "E, intra provincial"
    })
    void validateCreate_ShouldPass_WhenBusTypeIsAllowedForRouteType(
            String busType,
            String routeType
    ) {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, busType);
        Route route = routeWithId(2, routeType);
        ServiceType serviceType = serviceType("normal");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "C, inter provincial",
            "D, inter provincial",
            "E, inter provincial",
            "A+, inter provincial"
    })
    void validateCreate_ShouldThrowException_WhenBusTypeIsNotAllowedForRouteType(
            String busType,
            String routeType
    ) {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, busType);
        Route route = routeWithId(2, routeType);
        ServiceType serviceType = serviceType("normal");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    // =========================================================
    // CREATE - BUS TYPE & SERVICE TYPE
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "AA, luxury",
            "AA, super luxury",

            "A, normal",
            "B, normal",
            "B+, normal",
            "C, normal",
            "D, normal",
            "E, normal",

            "A, semi luxury",
            "B, semi luxury",
            "B+, semi luxury"
    })
    void validateCreate_ShouldPass_WhenBusTypeIsAllowedForServiceType(
            String busType,
            String serviceTypeName
    ) {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, busType);
        Route route = routeWithId(2, "intra provincial");
        ServiceType serviceType = serviceType(serviceTypeName);

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "A, luxury",
            "B, luxury",
            "C, luxury",
            "E, luxury",

            "A, super luxury",
            "B+, super luxury",

            "C, semi luxury",
            "D, semi luxury",
            "E, semi luxury"
    })
    void validateCreate_ShouldThrowException_WhenBusTypeIsNotAllowedForServiceType(
            String busType,
            String serviceTypeName
    ) {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, busType);
        Route route = routeWithId(2, "intra provincial");
        ServiceType serviceType = serviceType(serviceTypeName);

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    // =========================================================
    // CREATE - CASE / WHITESPACE NORMALIZATION
    // =========================================================

    @Test
    void validateCreate_ShouldPass_WhenBusTypeAndRouteTypeContainDifferentCase() {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, " aa ");
        Route route = routeWithId(2, " INTER PROVINCIAL ");
        ServiceType serviceType = serviceType(" LUXURY ");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenServiceTypeContainsDifferentCaseAndWhitespace() {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, "AA");
        Route route = routeWithId(2, "inter provincial");
        ServiceType serviceType = serviceType("  LuXuRy  ");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    // =========================================================
    // CREATE - UNKNOWN ROUTE / SERVICE TYPE
    // =========================================================

    @Test
    void validateCreate_ShouldThrowException_WhenRouteTypeIsUnknown() {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, "AA");
        Route route = routeWithId(2, "express");
        ServiceType serviceType = serviceType("luxury");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenServiceTypeIsUnknown() {
        // Arrange
        Vehicle vehicle = vehicleWithId(1, "AA");
        Route route = routeWithId(2, "intra provincial");
        ServiceType serviceType = serviceType("express");

        when(permitRepository
                .existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateCreate(
                        vehicle,
                        route,
                        serviceType
                )
        );
    }

    // =========================================================
    // INITIAL STATUS
    // =========================================================

    @Test
    void validateInitialStatus_ShouldPass_WhenStatusIsActive() {
        // Arrange
        PermiteStatus status = status("ACTIVE");

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_ShouldPass_WhenStatusIsActiveWithDifferentCase() {
        // Arrange
        PermiteStatus status = status("active");

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_ShouldThrowException_WhenStatusIsNotActive() {
        // Arrange
        PermiteStatus status = status("SUSPENDED");

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateInitialStatus(status)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "EXPIRED",
            "SUSPENDED",
            "TRANSFERRED"
    })
    void validateInitialStatus_ShouldThrowException_WhenStatusIsInvalid(
            String statusName
    ) {
        // Arrange
        PermiteStatus status = status(statusName);

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateInitialStatus(status)
        );
    }

    // =========================================================
    // STATUS TRANSITIONS - VALID
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusRemainsActive() {
        // Arrange
        PermiteStatus current = status("ACTIVE");
        PermiteStatus target = status("ACTIVE");

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenActiveChangesToExpired() {
        assertValidTransition("ACTIVE", "EXPIRED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenActiveChangesToSuspended() {
        assertValidTransition("ACTIVE", "SUSPENDED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenActiveChangesToTransferred() {
        assertValidTransition("ACTIVE", "TRANSFERRED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenSuspendedChangesToActive() {
        assertValidTransition("SUSPENDED", "ACTIVE");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenSuspendedChangesToExpired() {
        assertValidTransition("SUSPENDED", "EXPIRED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenSuspendedChangesToTransferred() {
        assertValidTransition("SUSPENDED", "TRANSFERRED");
    }

    // =========================================================
    // STATUS TRANSITIONS - INVALID
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "EXPIRED, ACTIVE",
            "EXPIRED, SUSPENDED",
            "EXPIRED, TRANSFERRED",

            "TRANSFERRED, ACTIVE",
            "TRANSFERRED, SUSPENDED",
            "TRANSFERRED, EXPIRED"
    })
    void validateStatusTransition_ShouldThrowException_WhenCurrentStatusIsTerminal(
            String currentStatus,
            String targetStatus
    ) {
        // Arrange
        PermiteStatus current = status(currentStatus);
        PermiteStatus target = status(targetStatus);

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "ACTIVE, ACTIVE",
            "ACTIVE, UNKNOWN",
            "SUSPENDED, UNKNOWN",
            "UNKNOWN, ACTIVE",
            "UNKNOWN, SUSPENDED"
    })
    void validateStatusTransition_ShouldThrowException_WhenTransitionIsInvalid(
            String currentStatus,
            String targetStatus
    ) {
        if (currentStatus.equals(targetStatus)) {
            assertDoesNotThrow(() ->
                    permitValidator.validateStatusTransition(
                            status(currentStatus),
                            status(targetStatus)
                    )
            );
            return;
        }

        assertThrows(
                BusinessRuleViolationException.class,
                () -> permitValidator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    // =========================================================
    // STATUS TRANSITIONS - NORMALIZATION
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusNamesContainDifferentCase() {
        // Arrange
        PermiteStatus current = status("active");
        PermiteStatus target = status("suspended");

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusNamesContainWhitespace() {
        // Arrange
        PermiteStatus current = status("  ACTIVE  ");
        PermiteStatus target = status("  EXPIRED  ");

        // Act & Assert
        assertDoesNotThrow(() ->
                permitValidator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private Vehicle vehicleWithId(Integer id, String busType) {
        return Vehicle.builder()
                .id(id)
                .bustype(
                        BusType.builder()
                                .name(busType)
                                .build()
                )
                .build();
    }

    private Route routeWithId(Integer id, String routeType) {
        return Route.builder()
                .id(id)
                .routetype(
                        RouteType.builder()
                                .name(routeType)
                                .build()
                )
                .build();
    }

    private ServiceType serviceType(String name) {
        return ServiceType.builder()
                .name(name)
                .build();
    }

    private PermiteStatus status(String name) {
        return PermiteStatus.builder()
                .name(name)
                .build();
    }

    private void assertValidTransition(
            String currentStatus,
            String targetStatus
    ) {
        assertDoesNotThrow(() ->
                permitValidator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }
}