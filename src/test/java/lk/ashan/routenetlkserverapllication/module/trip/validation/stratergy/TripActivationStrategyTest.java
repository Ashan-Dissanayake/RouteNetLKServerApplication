package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TripActivationStrategyTest {

    private TripStatusService tripStatusService;
    private TripRepository tripRepository;
    private TripStateTransitionHandler tripStateTransitionHandler;
    private TripActivationStrategy tripActivationStrategy;

    @BeforeEach
    void setUp() {
        tripStatusService = mock(TripStatusService.class);
        tripRepository = mock(TripRepository.class);
        tripStateTransitionHandler = mock(TripStateTransitionHandler.class);
        tripActivationStrategy = new TripActivationStrategy(tripStatusService, tripRepository, tripStateTransitionHandler);
    }

    @Test
    void activateTrip_ShouldThrowException_WhenVehicleStatusNotAllowed() {
        Trip trip = Trip.builder()
                .permite(Permite.builder()
                        .vehicle(Vehicle.builder()
                                .vehiclestatus(VehicleStatus.builder()
                                        .name("Unavailable")
                                        .build())
                                .build())
                        .build())
                .build();

        assertThrows(BusinessRuleViolationException.class, () -> tripActivationStrategy.activateTrip(trip));
    }

    @Test
    void activateTrip_ShouldThrowException_WhenActiveTripExists() {
        Trip trip = Trip.builder()
                .id(1)
                .permite(Permite.builder()
                        .route(Route.builder().id(1).build())
                        .vehicle(Vehicle.builder()
                                .vehiclestatus(VehicleStatus.builder()
                                        .name("Available")
                                        .build())
                                .build())
                        .build())
                .originterminal(Originterminal.builder().id(1).build())
                .todepature(LocalTime.now())
                .build();

        when(tripRepository.existsActiveTrip(1, 1, trip.getTodepature(), 1)).thenReturn(true);

        assertThrows(BusinessRuleViolationException.class, () -> tripActivationStrategy.activateTrip(trip));
    }

    @Test
    void activateTrip_ShouldActivateTrip_WhenValid() {
        Trip trip = Trip.builder()
                .id(1)
                .permite(Permite.builder()
                        .route(Route.builder().id(1).build())
                        .vehicle(Vehicle.builder()
                                .vehiclestatus(VehicleStatus.builder()
                                        .name("Available")
                                        .build())
                                .build())
                        .build())
                .originterminal(Originterminal.builder().id(1).build())
                .todepature(LocalTime.now())
                .build();

        when(tripRepository.existsActiveTrip(1, 1, trip.getTodepature(), 1)).thenReturn(false);
        when(tripStatusService.getByName("Active")).thenReturn(Tripstatus.builder().name("Active").build());

        tripActivationStrategy.activateTrip(trip);

        verify(tripStateTransitionHandler, times(1)).transitionTo(eq(trip), any(Tripstatus.class));
    }
}
