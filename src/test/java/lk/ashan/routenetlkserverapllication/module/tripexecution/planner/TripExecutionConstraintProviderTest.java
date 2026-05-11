package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

class TripExecutionConstraintProviderTest {

    private final ConstraintVerifier<TripExecutionConstraintProvider, TripExecutionSolution> constraintVerifier =
            ConstraintVerifier.build(
                    new TripExecutionConstraintProvider(),
                    TripExecutionSolution.class,
                    TripExecutionPlanning.class
            );

    // Helper to build the planning entity with Fact data
    private TripExecutionPlanning createPlanning(Integer id, LocalTime start, LocalTime end,
                                                 VehicleFact vehicle, CrewFact driver, RouteFact route) {
        TripExecutionPlanning planning = new TripExecutionPlanning();
        planning.setId(id);
        // Ensure these fields in TripExecutionPlanning are actually LocalTime
        planning.setDepartureTime(start);
        planning.setArrivalTime(end);
        planning.setVehicle(vehicle);
        planning.setDriver(driver);
        planning.setRoute(route);
        planning.setConductor(new CrewFact(99, 1, 1, 0));
        return planning;
    }

    @Test
    void vehicleOverlap_shouldPenalize() {
        VehicleFact v1 = new VehicleFact(1, "Luxury", 1000);
        LocalTime now = LocalTime.of(5, 5, 10, 0);

        TripExecutionPlanning trip1 = createPlanning(1, now, now.plusHours(2), v1, new CrewFact(), new RouteFact());
        TripExecutionPlanning trip2 = createPlanning(2, now.plusHours(1), now.plusHours(3), v1, new CrewFact(), new RouteFact());

        constraintVerifier.verifyThat(TripExecutionConstraintProvider::vehicleOverlap)
                .given(trip1, trip2)
                .penalizesBy(1);
    }

    @Test
    void driverFamiliarity_shouldPenalizeWhenLevelIsLow() {
        // Driver Level 1
        CrewFact driver = new CrewFact(10, 1, 1, 0);
        // Route requires Level 3
        RouteFact route = new RouteFact(100, 3, 50.0);

        TripExecutionPlanning planning = createPlanning(1, LocalTime.now(), LocalTime.now().plusHours(1),
                new VehicleFact(), driver, route);

        constraintVerifier.verifyThat(TripExecutionConstraintProvider::driverFamiliarity)
                .given(planning)
                .penalizesBy(1);
    }

    @Test
    void requiredRestPeriod_shouldPenalizeShortBreak() {
        CrewFact driver = new CrewFact(10, 3, 1, 0);

        // Trip 1 ends at 12:00
        TripExecutionPlanning t1 = createPlanning(1, LocalTime.of( 5, 5, 10, 0),
                LocalTime.of( 5, 5, 12, 0),
                new VehicleFact(), driver, new RouteFact());

        // Trip 2 starts at 12:15 (Only 15 mins gap, logic expects 30)
        TripExecutionPlanning t2 = createPlanning(2, LocalTime.of( 5, 5, 12, 15),
                LocalTime.of( 5, 5, 14, 0),
                new VehicleFact(), driver, new RouteFact());

        constraintVerifier.verifyThat(TripExecutionConstraintProvider::requiredRestPeriod)
                .given(t1, t2)
                .penalizesBy(1);
    }

    @Test
    void fairnessDriverDuty_shouldApplyQuadraticPenalty() {
        // Driver with 100 historical minutes
        CrewFact driver = new CrewFact(10, 3, 1, 100);

        // Correct LocalTime usage: .of(hour, minute)
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(9, 0); // Exactly 60 minutes difference

        TripExecutionPlanning planning = createPlanning(1, start, end, new VehicleFact(), driver, new RouteFact());

        // Expected: (100 + 60)^2 = 25600
        constraintVerifier.verifyThat(TripExecutionConstraintProvider::fairnessDriverDuty)
                .given(planning)
                .penalizesBy(25600);
    }
}
