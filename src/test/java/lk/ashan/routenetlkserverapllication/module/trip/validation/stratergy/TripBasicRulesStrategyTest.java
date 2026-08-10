package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Triptype;
import lk.ashan.routenetlkserverapllication.module.trip.repository.OriginTerminalRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TripBasicRulesStrategyTest {

    private TripRepository tripRepository;
    private PermitRepository permitRepository;
    private RouteRepository routeRepository;
    private OriginTerminalRepository originTerminalRepository;

    private TripBasicRulesStrategy strategy;

    @BeforeEach
    void setUp() {
        tripRepository = mock(TripRepository.class);
        permitRepository = mock(PermitRepository.class);
        routeRepository = mock(RouteRepository.class);
        originTerminalRepository = mock(OriginTerminalRepository.class);

        strategy = new TripBasicRulesStrategy(
                tripRepository,
                permitRepository,
                routeRepository,
                originTerminalRepository
        );

        /*
         * Default stubbing.
         *
         * validateCreate() executes validations sequentially, so these
         * defaults prevent unrelated validations from failing before the
         * validation currently being tested.
         */

        // Same route trips
        when(tripRepository.findByPermite_Route_Id(any()))
                .thenReturn(Collections.emptyList());

        // Duplicate check
        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        any(),
                        any(),
                        any(),
                        any(),
                        eq("Active")
                ))
                .thenReturn(false);

        // Active trip quota
        when(tripRepository.countByPermite_IdAndTripstatus_Name(any(), eq("Active")))
                .thenReturn(0L);

        // No overlapping trips
        when(tripRepository.findByPermite_Id(any()))
                .thenReturn(Collections.emptyList());

        // Valid route
        Route route = Route.builder()
                .origin("CityA")
                .destination("CityB")
                .mingapminutes(30)
                .build();

        when(routeRepository.findById(any()))
                .thenReturn(Optional.of(route));

        // Valid permit
        Permite permit = Permite.builder()
                .notripsperday(10)
                .route(route)
                .build();

        when(permitRepository.findById(any()))
                .thenReturn(Optional.of(permit));

        // Valid terminal
        Originterminal terminal = Originterminal.builder()
                .city("CityA")
                .build();

        when(originTerminalRepository.findById(any()))
                .thenReturn(Optional.of(terminal));
    }

    // -------------------------------------------------------------------------
    // validateTimeLogic()
    // -------------------------------------------------------------------------

    @Test
    void validateCreate_ShouldThrowException_WhenDepartureAndArrivalAreSame() {

        TripValidationContext context = validContextBuilder()
                .departure(LocalTime.of(10, 0))
                .arrival(LocalTime.of(10, 0))
                .triptypeId(1)
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertEquals(
                "Trip duration cannot be zero.",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenArrivalIsBeforeDepartureForNormalTrip() {

        TripValidationContext context = validContextBuilder()
                .departure(LocalTime.of(22, 0))
                .arrival(LocalTime.of(21, 0))
                .triptypeId(1)
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertEquals(
                "Arrival time is before departure, but this is not marked as a Midnight Trip.",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMidnightTripArrivalIsAfterDeparture() {

        TripValidationContext context = validContextBuilder()
                .departure(LocalTime.of(10, 0))
                .arrival(LocalTime.of(11, 0))
                .triptypeId(5)
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertEquals(
                "This is marked as a Midnight Trip, but the arrival time is not after midnight.",
                exception.getMessage()
        );
    }

    @Test
    void validateCreate_ShouldPassTimeLogic_WhenNormalTripTimesAreValid() {

        TripValidationContext context = validContextBuilder()
                .departure(LocalTime.of(10, 0))
                .arrival(LocalTime.of(11, 0))
                .triptypeId(1)
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldPassTimeLogic_WhenMidnightTripCrossesMidnight() {

        TripValidationContext context = validContextBuilder()
                .departure(LocalTime.of(23, 0))
                .arrival(LocalTime.of(1, 0))
                .triptypeId(5)
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    // -------------------------------------------------------------------------
    // validateSameRouteTripsMinGaps()
    // -------------------------------------------------------------------------

    @Test
    void validateCreate_ShouldThrowException_WhenSameRouteTripGapIsLessThanMinimum() {

        Route route = Route.builder()
                .mingapminutes(30)
                .origin("CityA")
                .destination("CityB")
                .build();

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        /*
         * Current strategy uses context.getId() when calling
         * findByPermite_Route_Id().
         */
        when(tripRepository.findByPermite_Route_Id(isNull()))
                .thenReturn(List.of(existingTrip));

        TripValidationContext context = validContextBuilder()
                .routeId(1)
                .departure(LocalTime.of(10, 15))
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertTrue(exception.getMessage().contains("Gap violation"));
        assertTrue(exception.getMessage().contains("15"));
    }

    @Test
    void validateCreate_ShouldPass_WhenSameRouteTripGapIsEnough() {

        Route route = Route.builder()
                .mingapminutes(30)
                .origin("CityA")
                .destination("CityB")
                .build();

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        when(tripRepository.findByPermite_Route_Id(isNull()))
                .thenReturn(List.of(existingTrip));

        TripValidationContext context = validContextBuilder()
                .routeId(1)
                .departure(LocalTime.of(10, 30))
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldSkipCurrentTrip_WhenUpdatingSameRouteTrip() {

        Route route = Route.builder()
                .mingapminutes(30)
                .origin("CityA")
                .destination("CityB")
                .build();

        Trip existingTrip = Trip.builder()
                .id(10)
                .todepature(LocalTime.of(10, 15))
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        when(tripRepository.findByPermite_Route_Id(10))
                .thenReturn(List.of(existingTrip));

        TripValidationContext context = validContextBuilder()
                .id(10)
                .routeId(1)
                .departure(LocalTime.of(10, 20))
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    // -------------------------------------------------------------------------
    // validateIdempotency()
    // -------------------------------------------------------------------------

    @Test
    void validateCreate_ShouldThrowException_WhenDuplicateTripExists() {

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        eq(1),
                        eq(1),
                        eq(LocalTime.of(10, 0)),
                        eq(LocalTime.of(11, 0)),
                        eq("Active")
                ))
                .thenReturn(true);

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .originTerminalId(1)
                .departure(LocalTime.of(10, 0))
                .arrival(LocalTime.of(11, 0))
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertTrue(exception.getMessage().contains("Duplicate Trip Detected"));
        assertTrue(exception.getMessage().contains("Permit 1"));
    }

    @Test
    void validateCreate_ShouldPass_WhenDuplicateTripDoesNotExist() {

        when(tripRepository
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        any(),
                        any(),
                        any(),
                        any(),
                        eq("Active")
                ))
                .thenReturn(false);

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .originTerminalId(1)
                .departure(LocalTime.of(10, 0))
                .arrival(LocalTime.of(11, 0))
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    // -------------------------------------------------------------------------
    // validatePermittedDailyTripQuota()
    // -------------------------------------------------------------------------

    @Test
    void validateCreate_ShouldThrowException_WhenDailyQuotaIsExceeded() {

        Permite permit = Permite.builder()
                .notripsperday(5)
                .route(Route.builder()
                        .origin("CityA")
                        .destination("CityB")
                        .mingapminutes(30)
                        .build())
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(tripRepository.countByPermite_IdAndTripstatus_Name(1, "Active"))
                .thenReturn(5L);

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertTrue(exception.getMessage().contains("Permit Quota Exceeded"));
        assertTrue(exception.getMessage().contains("5 trips per day"));
    }

    @Test
    void validateCreate_ShouldPass_WhenDailyQuotaIsNotExceeded() {

        Permite permit = Permite.builder()
                .notripsperday(5)
                .route(Route.builder()
                        .origin("CityA")
                        .destination("CityB")
                        .mingapminutes(30)
                        .build())
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(tripRepository.countByPermite_IdAndTripstatus_Name(1, "Active"))
                .thenReturn(4L);

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenPermitDoesNotExistForQuotaValidation() {

        when(permitRepository.findById(1))
                .thenReturn(Optional.empty());

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .build();

        assertThrows(
                ResourceNotFoundException.class,
                () -> strategy.validateCreate(context)
        );
    }

    // -------------------------------------------------------------------------
    // validateTripOverlap()
    // -------------------------------------------------------------------------

    @Test
    void validateCreate_ShouldThrowException_WhenTripsOverlap() {

        Triptype tripType = mock(Triptype.class);
        when(tripType.getId()).thenReturn(1);

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(tripType)
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .departure(LocalTime.of(10, 30))
                .arrival(LocalTime.of(11, 30))
                .triptypeId(1)
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertTrue(exception.getMessage().contains("Scheduling Conflict"));
        assertTrue(exception.getMessage().contains("10:00"));
        assertTrue(exception.getMessage().contains("11:00"));
    }

    @Test
    void validateCreate_ShouldPass_WhenTripsDoNotOverlap() {

        Triptype tripType = mock(Triptype.class);
        when(tripType.getId()).thenReturn(1);

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(tripType)
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .departure(LocalTime.of(11, 30))
                .arrival(LocalTime.of(12, 30))
                .triptypeId(1)
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldSkipCurrentTrip_WhenUpdatingOverlappingTrip() {

        Triptype tripType = mock(Triptype.class);
        when(tripType.getId()).thenReturn(1);

        Trip existingTrip = Trip.builder()
                .id(10)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(11, 0))
                .triptype(tripType)
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        TripValidationContext context = validContextBuilder()
                .id(10)
                .permitId(1)
                .departure(LocalTime.of(10, 30))
                .arrival(LocalTime.of(11, 30))
                .triptypeId(1)
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldDetectOvernightTripOverlap() {

        Triptype existingTripType = mock(Triptype.class);
        when(existingTripType.getId()).thenReturn(5);

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(23, 0))
                .toarrival(LocalTime.of(1, 0))
                .triptype(existingTripType)
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .departure(LocalTime.of(23, 30))
                .arrival(LocalTime.of(2, 0))
                .triptypeId(5)
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertTrue(exception.getMessage().contains("Scheduling Conflict"));
    }

    // -------------------------------------------------------------------------
    // validateTerminalLocation()
    // -------------------------------------------------------------------------

    @Test
    void validateCreate_ShouldThrowException_WhenTerminalIsNotAuthorized() {

        Route route = Route.builder()
                .origin("CityA")
                .destination("CityB")
                .mingapminutes(30)
                .build();

        Permite permit = Permite.builder()
                .notripsperday(10)
                .route(route)
                .build();

        Originterminal terminal = Originterminal.builder()
                .city("CityC")
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(terminal));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .originTerminalId(1)
                .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validateCreate(context)
        );

        assertTrue(exception.getMessage().contains("Terminal Mismatch"));
        assertTrue(exception.getMessage().contains("CityC"));
    }

    @Test
    void validateCreate_ShouldPass_WhenTerminalMatchesRouteOrigin() {

        Route route = Route.builder()
                .origin("CityA")
                .destination("CityB")
                .mingapminutes(30)
                .build();

        Permite permit = Permite.builder()
                .notripsperday(10)
                .route(route)
                .build();

        Originterminal terminal = Originterminal.builder()
                .city("CityA")
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(terminal));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .originTerminalId(1)
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenTerminalMatchesRouteDestination() {

        Route route = Route.builder()
                .origin("CityA")
                .destination("CityB")
                .mingapminutes(30)
                .build();

        Permite permit = Permite.builder()
                .notripsperday(10)
                .route(route)
                .build();

        Originterminal terminal = Originterminal.builder()
                .city("CityB")
                .build();

        when(permitRepository.findById(1))
                .thenReturn(Optional.of(permit));

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.of(terminal));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .originTerminalId(1)
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenTerminalDoesNotExist() {

        when(originTerminalRepository.findById(1))
                .thenReturn(Optional.empty());

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .originTerminalId(1)
                .build();

        assertThrows(
                ResourceNotFoundException.class,
                () -> strategy.validateCreate(context)
        );
    }

    // -------------------------------------------------------------------------
    // validatePermitTripSequence()
    //
    // This method is NOT called from validateCreate() in the current strategy.
    // Therefore it is tested separately using reflection.
    // -------------------------------------------------------------------------

    @Test
    void validatePermitTripSequence_ShouldThrowException_WhenTurnaroundTimeIsInsufficient()
            throws Exception {

        Triptype tripType = mock(Triptype.class);
        when(tripType.getId()).thenReturn(1);

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(10, 30))
                .triptype(tripType)
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        Route route = Route.builder()
                .mingapminutes(30)
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .routeId(1)
                .departure(LocalTime.of(10, 45))
                .arrival(LocalTime.of(11, 30))
                .triptypeId(1)
                .build();

        Exception exception = assertThrows(
                Exception.class,
                () -> invokePrivateMethod(
                        "validatePermitTripSequence",
                        context
                )
        );

        Throwable cause = getRootCause(exception);

        assertInstanceOf(
                BusinessRuleViolationException.class,
                cause
        );

        assertTrue(cause.getMessage().contains("Insufficient turnaround time"));
    }

    @Test
    void validatePermitTripSequence_ShouldPass_WhenTurnaroundTimeIsEnough()
            throws Exception {

        Triptype tripType = mock(Triptype.class);
        when(tripType.getId()).thenReturn(1);

        Trip existingTrip = Trip.builder()
                .id(100)
                .todepature(LocalTime.of(10, 0))
                .toarrival(LocalTime.of(10, 30))
                .triptype(tripType)
                .build();

        when(tripRepository.findByPermite_Id(1))
                .thenReturn(List.of(existingTrip));

        Route route = Route.builder()
                .mingapminutes(30)
                .build();

        when(routeRepository.findById(1))
                .thenReturn(Optional.of(route));

        TripValidationContext context = validContextBuilder()
                .permitId(1)
                .routeId(1)
                .departure(LocalTime.of(11, 0))
                .arrival(LocalTime.of(12, 0))
                .triptypeId(1)
                .build();

        assertDoesNotThrow(() ->
                invokePrivateMethod(
                        "validatePermitTripSequence",
                        context
                )
        );
    }

    // -------------------------------------------------------------------------
    // Full validation test
    // -------------------------------------------------------------------------

    @Test
    void validateCreate_ShouldPass_WhenAllRulesAreSatisfied() {

        TripValidationContext context = validContextBuilder()
                .id(null)
                .permitId(1)
                .routeId(1)
                .originTerminalId(1)
                .triptypeId(1)
                .departure(LocalTime.of(10, 0))
                .arrival(LocalTime.of(11, 0))
                .build();

        assertDoesNotThrow(() -> strategy.validateCreate(context));

        verify(tripRepository, atLeastOnce())
                .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                        1,
                        1,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        "Active"
                );

        verify(tripRepository, atLeastOnce())
                .countByPermite_IdAndTripstatus_Name(1, "Active");

        verify(permitRepository, atLeastOnce())
                .findById(1);

        verify(originTerminalRepository, atLeastOnce())
                .findById(1);
    }

    // -------------------------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------------------------

    private TripValidationContext.TripValidationContextBuilder validContextBuilder() {

        return TripValidationContext.builder()
                .id(null)
                .permitId(1)
                .routeId(1)
                .originTerminalId(1)
                .triptypeId(1)
                .departure(LocalTime.of(10, 0))
                .arrival(LocalTime.of(11, 0));
    }

    private Object invokePrivateMethod(
            String methodName,
            TripValidationContext context
    ) throws Exception {

        Method method = TripBasicRulesStrategy.class.getDeclaredMethod(
                methodName,
                TripValidationContext.class
        );

        method.setAccessible(true);

        try {
            return method.invoke(strategy, context);
        } catch (InvocationTargetException e) {
            throw e;
        }
    }

    private Throwable getRootCause(Throwable throwable) {

        Throwable cause = throwable;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }
}
