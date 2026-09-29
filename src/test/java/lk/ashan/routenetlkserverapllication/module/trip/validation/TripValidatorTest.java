package lk.ashan.routenetlkserverapllication.module.trip.validation;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Triptype;
import lk.ashan.routenetlkserverapllication.module.trip.repository.OriginTerminalRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionService;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripValidatorTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private PermitRepository permitRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private OriginTerminalRepository originTerminalRepository;

    @Mock
    private TripExecutionService tripExecutionService;

    @InjectMocks
    private TripValidator tripValidator;


    // =========================================================
    // VALIDATE CREATE - TIME LOGIC
    // =========================================================

    @Test
    void validateCreate_ShouldThrowException_WhenDepartureAndArrivalAreSame() {

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(10, 0))
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertEquals(
                "Trip duration cannot be zero.",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenArrivalIsBeforeDepartureForNormalTrip() {

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(22, 0))
                .toarrival(LocalTime.of(21, 0))
                .triptype(
                        Triptype.builder()
                                .id(1)
                                .build()
                )
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertEquals(
                "Arrival time is before departure, "
                        + "but this is not marked as a Midnight Trip.",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMidnightTripDoesNotCrossMidnight() {

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(
                        Triptype.builder()
                                .id(5)
                                .build()
                )
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertEquals(
                "This is marked as a Midnight Trip, "
                        + "but the arrival time is not after midnight.",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenNormalTripTimesAreValid() {


        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(
                        Triptype.builder()
                                .id(1)
                                .build()
                )
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenMidnightTripCrossesMidnight() {

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(23, 0))
                .toarrival(LocalTime.of(1, 0))
                .triptype(
                        Triptype.builder()
                                .id(5)
                                .build()
                )
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    // =========================================================
    // VALIDATE CREATE - SAME ROUTE MINIMUM GAP
    // =========================================================


    @Test
    void validateCreate_ShouldPass_WhenSameRouteTripGapEqualsMinimum() {


        Route route = Route.builder()
                .id(1)
                .origin("CityA")
                .destination("CityB")
                .mingapminutes(30)
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .build();

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(List.of(existingTrip));

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(10, 30))
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenUpdatingSameTrip() {

        Route route = Route.builder()
                .id(1)
                .origin("CityA")
                .destination("CityB")
                .mingapminutes(30)
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        Trip existingTrip = Trip.builder()
                .id(10)
                .todepature(LocalTime.of(10, 15))
                .build();

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(List.of(existingTrip));

        Trip trip = validTripBuilder()
                .id(10)
                .todepature(LocalTime.of(10, 20))
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    // =========================================================
    // VALIDATE CREATE - IDEMPOTENCY
    // =========================================================

    @Test
    void validateCreate_ShouldThrowException_WhenDuplicateActiveTripExists() {

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(validRoute()));

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(Collections.emptyList());

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        "Active"
                ))
                .thenReturn(true);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Duplicate Trip Detected")
        );

        assertTrue(
                exception.getMessage()
                        .contains("Permit 1")
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenDuplicateActiveTripDoesNotExist() {


        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        any(),
                        any(),
                        any(),
                        any(),
                        eq("Active")
                ))
                .thenReturn(false);

        Trip trip = validTripBuilder()
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    // =========================================================
    // VALIDATE CREATE - DAILY TRIP QUOTA
    // =========================================================

    @Test
    void validateCreate_ShouldThrowException_WhenDailyTripQuotaIsExceeded() {

        Trip trip = validTripBuilder().build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(validRoute()));

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(Collections.emptyList());

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        "Active"
                ))
                .thenReturn(false);

        Permite permit = validPermitBuilder()
                .notripsperday(5)
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(tripRepository.countByPermite_IdAndTripstatus_Name(
                1,
                "Active"
        )).thenReturn(5L);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Permit Quota Exceeded")
        );

        assertTrue(
                exception.getMessage()
                        .contains("5 trips per day")
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenDailyTripQuotaIsNotExceeded() {


        Permite permit = validPermitBuilder()
                .notripsperday(5)
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(tripRepository.countByPermite_IdAndTripstatus_Name(
                1,
                "Active"
        )).thenReturn(4L);

        Trip trip = validTripBuilder()
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenPermitDoesNotExist() {

        Trip trip = validTripBuilder().build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(validRoute()));

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(Collections.emptyList());

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        "Active"
                ))
                .thenReturn(false);

        when(permitRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertEquals(
                "Permit Not Found",
                exception.getMessage()
        );
    }

    // =========================================================
    // VALIDATE CREATE - TRIP OVERLAP
    // =========================================================

    @Test
    void validateCreate_ShouldThrowException_WhenTripsOverlap() {

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(
                        Triptype.builder()
                                .id(1)
                                .build()
                )
                .build();

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(10, 30))
                .toarrival(LocalTime.of(11, 30))
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(validRoute()));

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(Collections.emptyList());

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 30),
                        "Active"
                ))
                .thenReturn(false);

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(validPermit()));

        when(tripRepository.countByPermite_IdAndTripstatus_Name(
                1,
                "Active"
        )).thenReturn(0L);

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Scheduling Conflict")
        );

        assertTrue(
                exception.getMessage()
                        .contains("10:00")
        );

        assertTrue(
                exception.getMessage()
                        .contains("11:00")
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenTripsDoNotOverlap() {

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(
                        Triptype.builder()
                                .id(1)
                                .build()
                )
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(11, 30))
                .toarrival(LocalTime.of(12, 30))
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenUpdatingSameTripThatWouldOtherwiseOverlap() {

        Trip existingTrip = Trip.builder()
                .id(10)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(
                        Triptype.builder()
                                .id(1)
                                .build()
                )
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        Trip trip = validTripBuilder()
                .id(10)
                .todepature(LocalTime.of(10, 30))
                .toarrival(LocalTime.of(11, 30))
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }


    // =========================================================
    // VALIDATE CREATE - TERMINAL LOCATION
    // =========================================================

    @Test
    void validateCreate_ShouldThrowException_WhenTerminalIsNotAuthorizedForRoute() {

        Trip trip = validTripBuilder().build();

        Permite permit = validPermitBuilder()
                .route(
                        Route.builder()
                                .id(1)
                                .origin("CityA")
                                .destination("CityB")
                                .mingapminutes(30)
                                .build()
                )
                .build();

        Originterminal terminal = Originterminal.builder()
                .id(1)
                .city("CityC")
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(validRoute()));

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(Collections.emptyList());

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        "Active"
                ))
                .thenReturn(false);

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(tripRepository.countByPermite_IdAndTripstatus_Name(
                1,
                "Active"
        )).thenReturn(0L);

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(Collections.emptyList());

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(terminal));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateCreate(trip)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Terminal Mismatch")
        );

        assertTrue(
                exception.getMessage()
                        .contains("CityC")
        );
    }
    @Test
    void validateCreate_ShouldPass_WhenTerminalMatchesRouteOrigin() {

        Permite permit = validPermitBuilder()
                .route(
                        Route.builder()
                                .origin("CityA")
                                .destination("CityB")
                                .mingapminutes(30)
                                .build()
                )
                .build();

        Originterminal terminal = Originterminal.builder()
                .id(1)
                .city("CityA")
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(terminal));

        Trip trip = validTripBuilder()
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenTerminalMatchesRouteDestination() {

        Permite permit = validPermitBuilder()
                .route(
                        Route.builder()
                                .origin("CityA")
                                .destination("CityB")
                                .mingapminutes(30)
                                .build()
                )
                .build();

        Originterminal terminal = Originterminal.builder()
                .id(1)
                .city("CityB")
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(terminal));

        Trip trip = validTripBuilder()
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenTerminalMatchesRouteCaseInsensitively() {

        Permite permit = validPermitBuilder()
                .route(
                        Route.builder()
                                .origin("CityA")
                                .destination("CityB")
                                .mingapminutes(30)
                                .build()
                )
                .build();

        Originterminal terminal = Originterminal.builder()
                .id(1)
                .city("citya")
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(terminal));

        Trip trip = validTripBuilder()
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }

    @Test
    void validateCreate_ShouldPass_WhenTurnaroundTimeIsExactlyMinimum() {

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(10, 30))
                .triptype(
                        Triptype.builder()
                                .id(1)
                                .build()
                )
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        Route route = Route.builder()
                .id(1)
                .mingapminutes(30)
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        Trip trip = validTripBuilder()
                .todepature(LocalTime.of(11, 0))
                .toarrival(LocalTime.of(12, 0))
                .build();

        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );
    }


    // =========================================================
    // VALIDATE ACTIVATION
    // =========================================================

    @Test
    void validateActivation_ShouldPass_WhenVehicleIsAvailable() {

        Trip trip = validTripBuilder()
                .id(1)
                .build();

        when(tripRepository.existsActiveTrip(
                1,
                1,
                LocalTime.of(10, 0),
                1
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> tripValidator.validateActivation(trip)
        );
    }

    @Test
    void validateActivation_ShouldPass_WhenVehicleIsAllocated() {

        Trip trip = validTripBuilder()
                .id(1)
                .build();

        trip.getPermite()
                .getVehicle()
                .getVehiclestatus()
                .setName("Allocated");

        when(tripRepository.existsActiveTrip(
                1,
                1,
                LocalTime.of(10, 0),
                1
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> tripValidator.validateActivation(trip)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "UNAVAILABLE",
            "MAINTENANCE",
            "SUSPENDED",
            "OUT OF SERVICE",
            "UNKNOWN"
    })
    void validateActivation_ShouldThrowException_WhenVehicleStatusIsNotAllowed(
            String vehicleStatus
    ) {

        Trip trip = validTripWithVehicleStatus(vehicleStatus);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateActivation(trip)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Cannot activate trip")
        );

        assertTrue(
                exception.getMessage()
                        .contains(vehicleStatus)
        );

        verifyNoInteractions(tripRepository);
    }

    @Test
    void validateActivation_ShouldThrowException_WhenActiveTripAlreadyExists() {

        Trip trip = validTripBuilder()
                .id(1)
                .build();

        when(tripRepository.existsActiveTrip(
                1,
                1,
                LocalTime.of(10, 0),
                1
        )).thenReturn(true);

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateActivation(trip)
        );

        assertEquals(
                "An active trip already exists for the same route, "
                        + "origin terminal and departure time.",
                exception.getMessage()
        );
    }

    @Test
    void validateActivation_ShouldHandleVehicleStatusCaseInsensitively() {

        Trip trip = validTripBuilder()
                .id(1)
                .build();

        trip.getPermite()
                .getVehicle()
                .getVehiclestatus()
                .setName("allocated");

        when(tripRepository.existsActiveTrip(
                1,
                1,
                LocalTime.of(10, 0),
                1
        )).thenReturn(false);

        assertDoesNotThrow(
                () -> tripValidator.validateActivation(trip)
        );
    }

    // =========================================================
    // VALIDATE SUSPENSION
    // =========================================================

    @Test
    void validateSuspension_ShouldThrowException_WhenTripIsInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecution execution = executionWithStatus("IN PROGRESS");

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(execution));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateSuspension(trip)
        );

        assertEquals(
                "Cannot suspend the Master Schedule because "
                        + "there are trips currently 'IN PROGRESS' on the road.",
                exception.getMessage()
        );
    }

    @Test
    void validateSuspension_ShouldPass_WhenNoTripIsInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("COMPLETED")
                ));

        assertDoesNotThrow(
                () -> tripValidator.validateSuspension(trip)
        );
    }

    @Test
    void validateSuspension_ShouldPass_WhenNoExecutionsExist() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(
                () -> tripValidator.validateSuspension(trip)
        );
    }

    @Test
    void validateSuspension_ShouldThrowException_WhenOneOfMultipleExecutionsIsInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("COMPLETED"),
                        executionWithStatus("CANCELLED"),
                        executionWithStatus("IN PROGRESS")
                ));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateSuspension(trip)
        );
    }

    @Test
    void validateSuspension_ShouldDetectInProgressCaseInsensitively() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("in progress")
                ));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateSuspension(trip)
        );
    }

    // =========================================================
    // VALIDATE DISCONTINUATION
    // =========================================================

    @Test
    void validateDiscontinuation_ShouldThrowException_WhenTripIsInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("IN PROGRESS")
                ));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateDiscontinuation(trip)
        );

        assertEquals(
                "Cannot discontinue: A vehicle is currently "
                        + "performing a journey for this route.",
                exception.getMessage()
        );
    }

    @Test
    void validateDiscontinuation_ShouldThrowException_WhenCompletedExecutionExists() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("COMPLETED")
                ));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateDiscontinuation(trip)
        );

        assertEquals(
                "Cannot discontinue: There are completed trips "
                        + "awaiting fare collection/settlement.",
                exception.getMessage()
        );
    }

    @Test
    void validateDiscontinuation_ShouldPass_WhenNoLiveOrCompletedTripsExist() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("CANCELLED")
                ));

        assertDoesNotThrow(
                () -> tripValidator.validateDiscontinuation(trip)
        );
    }

    @Test
    void validateDiscontinuation_ShouldPass_WhenNoExecutionsExist() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(
                () -> tripValidator.validateDiscontinuation(trip)
        );
    }

    @Test
    void validateDiscontinuation_ShouldPrioritizeInProgressOverCompleted() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("COMPLETED"),
                        executionWithStatus("IN PROGRESS")
                ));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateDiscontinuation(trip)
        );

        assertEquals(
                "Cannot discontinue: A vehicle is currently "
                        + "performing a journey for this route.",
                exception.getMessage()
        );
    }

    @Test
    void validateDiscontinuation_ShouldDetectCompletedCaseInsensitively() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        executionWithStatus("completed")
                ));

        assertThrows(
                BusinessRuleViolationException.class,
                () -> tripValidator.validateDiscontinuation(trip)
        );
    }

    // =========================================================
    // STATUS TRANSITIONS - SAME STATUS
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "DRAFT",
            "ACTIVE",
            "SUSPENDED",
            "DISCONTINUED"
    })
    void validateStatusTransition_ShouldPass_WhenStatusRemainsSame(
            String statusName
    ) {

        assertDoesNotThrow(() ->
                tripValidator.validateStatusTransition(
                        status(statusName),
                        status(statusName)
                )
        );
    }

    // =========================================================
    // STATUS TRANSITIONS - VALID
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenDraftChangesToActive() {
        assertValidStatusTransition("DRAFT", "ACTIVE");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenDraftChangesToCancelled() {
        assertValidStatusTransition("DRAFT", "CANCELLED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenActiveChangesToSuspended() {
        assertValidStatusTransition("ACTIVE", "SUSPENDED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenActiveChangesToDiscontinued() {
        assertValidStatusTransition("ACTIVE", "DISCONTINUED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenActiveChangesToDraft() {
        assertValidStatusTransition("ACTIVE", "DRAFT");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenSuspendedChangesToActive() {
        assertValidStatusTransition("SUSPENDED", "ACTIVE");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenSuspendedChangesToDiscontinued() {
        assertValidStatusTransition("SUSPENDED", "DISCONTINUED");
    }

    // =========================================================
    // STATUS TRANSITIONS - INVALID
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "DRAFT, SUSPENDED",
            "DRAFT, DISCONTINUED",

            "ACTIVE, CANCELLED",

            "SUSPENDED, DRAFT",

            "DISCONTINUED, DRAFT",
            "DISCONTINUED, ACTIVE",
            "DISCONTINUED, SUSPENDED"
    })
    void validateStatusTransition_ShouldThrowException_WhenTransitionIsInvalid(
            String currentStatus,
            String targetStatus
    ) {

        InvalidStateTransitionException exception = assertThrows(
                InvalidStateTransitionException.class,
                () -> tripValidator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );

        assertTrue(
                exception.getMessage()
                        .contains(currentStatus)
        );

        assertTrue(
                exception.getMessage()
                        .contains(targetStatus)
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCurrentStatusIsUnknown() {

        InvalidStateTransitionException exception = assertThrows(
                InvalidStateTransitionException.class,
                () -> tripValidator.validateStatusTransition(
                        status("UNKNOWN"),
                        status("ACTIVE")
                )
        );

        assertEquals(
                "Invalid status transition from UNKNOWN to ACTIVE",
                exception.getMessage()
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenTargetStatusIsUnknown() {

        InvalidStateTransitionException exception = assertThrows(
                InvalidStateTransitionException.class,
                () -> tripValidator.validateStatusTransition(
                        status("ACTIVE"),
                        status("UNKNOWN")
                )
        );

        assertEquals(
                "Invalid status transition from ACTIVE to UNKNOWN",
                exception.getMessage()
        );
    }

    // =========================================================
    // STATUS TRANSITIONS - NORMALIZATION
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "draft, active",
            "ACTIVE, suspended",
            "sUsPeNdEd, discontinued"
    })
    void validateStatusTransition_ShouldPass_WhenStatusNamesHaveDifferentCase(
            String currentStatus,
            String targetStatus
    ) {

        assertDoesNotThrow(() ->
                tripValidator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusNamesContainWhitespace() {

        assertDoesNotThrow(() ->
                tripValidator.validateStatusTransition(
                        status("  ACTIVE  "),
                        status("  SUSPENDED  ")
                )
        );
    }

    // =========================================================
    // FULL CREATE VALIDATION
    // =========================================================

    @Test
    void validateCreate_ShouldPass_WhenAllBusinessRulesAreSatisfied() {

        Trip trip = validTripBuilder()
                .build();
        stubValidCreateDependencies(
                trip.getTodepature(),
                trip.getToarrival()
        );

        assertDoesNotThrow(
                () -> tripValidator.validateCreate(trip)
        );

        verify(routeRepository, atLeastOnce())
                .findById(1);

        verify(tripRepository, atLeastOnce())
                .findByPermite_Route_Id(1);

        verify(tripRepository, atLeastOnce())
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        "Active"
                );

        verify(permitRepository, atLeastOnce())
                .findById(1);

        verify(tripRepository, atLeastOnce())
                .countByPermite_IdAndTripstatus_Name(
                        1,
                        "Active"
                );

        verify(tripRepository, atLeastOnce())
                .findByPermite_Id(1);

        verify(originTerminalRepository, atLeastOnce())
                .findById(1);
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private Trip.TripBuilder validTripBuilder() {
        return Trip.builder()
                .id(null)
                .permite(validPermit())
                .originterminal(validOriginTerminal())
                .triptype(
                        Triptype.builder()
                                .id(1)
                                .build()
                )
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0));
    }

    private Permite.PermiteBuilder validPermitBuilder() {

        return Permite.builder()
                .id(1)
                .notripsperday(10)
                .route(validRoute())
                .vehicle(
                        Vehicle.builder()
                                .vehiclestatus(
                                        VehicleStatus.builder()
                                                .name("Available")
                                                .build()
                                )
                                .build()
                );
    }

    private Route validRoute() {
        return Route.builder()
                .id(1)
                .origin("CityA")
                .destination("CityB")
                .mingapminutes(30)
                .build();
    }

    private Permite validPermit() {
        return validPermitBuilder().build();
    }

    private Originterminal validOriginTerminal() {
        return Originterminal.builder()
                .id(1)
                .city("CityA")
                .build();
    }

    private Trip validTripWithVehicleStatus(String status) {

        Trip trip = validTripBuilder().build();

        trip.getPermite()
                .getVehicle()
                .getVehiclestatus()
                .setName(status);

        return trip;
    }

    private TripExecution executionWithStatus(String statusName) {

        return TripExecution.builder()
                .tripexecutionstatus(
                        TripExecutionStatus.builder()
                                .name(statusName)
                                .build()
                )
                .build();
    }

    private Tripstatus status(String name) {

        return Tripstatus.builder()
                .name(name)
                .build();
    }

    private void assertValidStatusTransition(
            String currentStatus,
            String targetStatus
    ) {
        assertDoesNotThrow(() ->
                tripValidator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    private void stubValidCreateDependencies(
            LocalTime departure,
            LocalTime arrival
    ) {

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(validRoute()));

        when(tripRepository.findByPermite_Route_Id(1))
                .thenReturn(Collections.emptyList());

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        departure,
                        arrival,
                        "Active"
                ))
                .thenReturn(false);

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(validPermit()));

        when(tripRepository.countByPermite_IdAndTripstatus_Name(
                1,
                "Active"
        )).thenReturn(0L);

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(Collections.emptyList());

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(validOriginTerminal()));
    }


}