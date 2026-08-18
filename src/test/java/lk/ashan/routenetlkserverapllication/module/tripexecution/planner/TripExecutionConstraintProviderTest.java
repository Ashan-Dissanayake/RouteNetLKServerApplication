package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;


import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

class TripExecutionConstraintProviderTest {

    private ConstraintVerifier<TripExecutionConstraintProvider, TripExecutionSolution>
            constraintVerifier;

    private VehicleFact vehicle1;
    private VehicleFact vehicle2;

    private CrewFact driver1;
    private CrewFact driver2;

    private CrewFact conductor1;
    private CrewFact conductor2;

    private RouteFact route;

    @BeforeEach
    void setUp() {

        constraintVerifier = ConstraintVerifier.build(
                new TripExecutionConstraintProvider(),
                TripExecutionSolution.class,
                TripExecutionPlanning.class
        );

        vehicle1 = new VehicleFact(
                1001,
                "NORMAL",
                50000
        );

        vehicle2 = new VehicleFact(
                1002,
                "NORMAL",
                60000
        );

        driver1 = new CrewFact(
                2001,
                3,
                1,
                0
        );

        driver2 = new CrewFact(
                2002,
                3,
                1,
                0
        );

        conductor1 = new CrewFact(
                3001,
                3,
                2,
                0
        );

        conductor2 = new CrewFact(
                3002,
                3,
                2,
                0
        );

        route = new RouteFact(
                4001,
                2,
                50.0
        );
    }


    // ============================================================
    // Helper Method
    // ============================================================

    private TripExecutionPlanning trip(
            Integer id,
            RouteFact route,
            VehicleFact vehicle,
            CrewFact driver,
            CrewFact conductor,
            LocalTime departure,
            LocalTime arrival
    ) {

        TripExecutionPlanning planning =
                new TripExecutionPlanning();

        planning.setId(id);
        planning.setRoute(route);
        planning.setVehicle(vehicle);
        planning.setDriver(driver);
        planning.setConductor(conductor);
        planning.setDepartureTime(departure);
        planning.setArrivalTime(arrival);

        return planning;
    }


    // ============================================================
    // 1. VEHICLE OVERLAP
    // ============================================================

    @Test
    void vehicleOverlap_shouldPenalizeOverlappingTripsForSameVehicle() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle1,
                        driver2,
                        conductor2,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::vehicleOverlap)
                .given(trip1, trip2)
                .penalizesBy(1);
    }


    @Test
    void vehicleOverlap_shouldNotPenalizeDifferentVehicles() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle2,
                        driver2,
                        conductor2,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::vehicleOverlap)
                .given(trip1, trip2)
                .penalizesBy(0);
    }


    // ============================================================
    // 2. DRIVER OVERLAP
    // ============================================================

    @Test
    void driverOverlap_shouldPenalizeOverlappingTripsForSameDriver() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle2,
                        driver1,
                        conductor2,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::driverOverlap)
                .given(trip1, trip2)
                .penalizesBy(1);
    }


    @Test
    void driverOverlap_shouldNotPenalizeDifferentDrivers() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle2,
                        driver2,
                        conductor2,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::driverOverlap)
                .given(trip1, trip2)
                .penalizesBy(0);
    }


    // ============================================================
    // 3. CONDUCTOR OVERLAP
    // ============================================================

    @Test
    void conductorOverlap_shouldPenalizeOverlappingTripsForSameConductor() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle2,
                        driver2,
                        conductor1,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::conductorOverlap)
                .given(trip1, trip2)
                .penalizesBy(1);
    }


    @Test
    void conductorOverlap_shouldNotPenalizeDifferentConductors() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle2,
                        driver2,
                        conductor2,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::conductorOverlap)
                .given(trip1, trip2)
                .penalizesBy(0);
    }


    // ============================================================
    // 4. DRIVER FAMILIARITY
    // ============================================================

    @Test
    void driverFamiliarity_shouldPenalizeDriverWithInsufficientFamiliarity() {

        CrewFact lowFamiliarityDriver =
                new CrewFact(
                        2003,
                        1,
                        1,
                        0
                );

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        lowFamiliarityDriver,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::driverFamiliarity)
                .given(planning)
                .penalizesBy(1);
    }


    @Test
    void driverFamiliarity_shouldNotPenalizeDriverWithRequiredFamiliarity() {

        CrewFact qualifiedDriver =
                new CrewFact(
                        2004,
                        2,
                        1,
                        0
                );

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        qualifiedDriver,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::driverFamiliarity)
                .given(planning)
                .penalizesBy(0);
    }


    // ============================================================
    // 5. CONDUCTOR FAMILIARITY
    // ============================================================

    @Test
    void conductorFamiliarity_shouldPenalizeConductorWithInsufficientFamiliarity() {

        CrewFact lowFamiliarityConductor =
                new CrewFact(
                        3003,
                        1,
                        2,
                        0
                );

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        lowFamiliarityConductor,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::conductorFamiliarity)
                .given(planning)
                .penalizesBy(1);
    }


    @Test
    void conductorFamiliarity_shouldNotPenalizeQualifiedConductor() {

        CrewFact qualifiedConductor =
                new CrewFact(
                        3004,
                        2,
                        2,
                        0
                );

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        qualifiedConductor,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::conductorFamiliarity)
                .given(planning)
                .penalizesBy(0);
    }


    // ============================================================
    // 6. REQUIRED REST PERIOD
    // ============================================================

    @Test
    void requiredRestPeriod_shouldPenalizeDriverWithLessThanThirtyMinutesRest() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle2,
                        driver1,
                        conductor2,
                        LocalTime.of(10, 20),
                        LocalTime.of(12, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::requiredRestPeriod)
                .given(trip1, trip2)
                .penalizesBy(1);
    }


    @Test
    void requiredRestPeriod_shouldNotPenalizeDriverWithThirtyMinutesRest() {

        TripExecutionPlanning trip1 =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        TripExecutionPlanning trip2 =
                trip(
                        2,
                        route,
                        vehicle2,
                        driver1,
                        conductor2,
                        LocalTime.of(10, 30),
                        LocalTime.of(12, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::requiredRestPeriod)
                .given(trip1, trip2)
                .penalizesBy(0);
    }


    // ============================================================
    // 7. DRIVER WORKLOAD FAIRNESS
    // ============================================================

    @Test
    void fairnessDriverDuty_shouldPenalizeBasedOnTotalDutyMinutes() {

        /*
         * Existing duty = 60 minutes
         * New trip duty = 120 minutes
         *
         * Total = 60 + 120 = 180
         *
         * Penalty = 180² = 32400
         */

        driver1.setTotalDutyMinutes(60);

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::fairnessDriverDuty)
                .given(planning)
                .penalizesBy(32400);
    }


    @Test
    void fairnessDriverDuty_shouldUseZeroWhenExistingDutyIsNull() {

        driver1.setTotalDutyMinutes(null);

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0)
                );

        /*
         * Existing = 0
         * New = 60
         * Total = 60
         * Penalty = 60² = 3600
         */

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::fairnessDriverDuty)
                .given(planning)
                .penalizesBy(3600);
    }


    // ============================================================
    // 8. CONDUCTOR WORKLOAD FAIRNESS
    // ============================================================

    @Test
    void fairnessConductorDuty_shouldPenalizeBasedOnTotalDutyMinutes() {

        /*
         * Existing duty = 60 minutes
         * New trip duty = 120 minutes
         *
         * Total = 180
         *
         * Penalty = 180² = 32400
         */

        conductor1.setTotalDutyMinutes(60);

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(10, 0)
                );

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::fairnessConductorDuty)
                .given(planning)
                .penalizesBy(32400);
    }


    @Test
    void fairnessConductorDuty_shouldUseZeroWhenExistingDutyIsNull() {

        conductor1.setTotalDutyMinutes(null);

        TripExecutionPlanning planning =
                trip(
                        1,
                        route,
                        vehicle1,
                        driver1,
                        conductor1,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0)
                );

        /*
         * Existing = 0
         * New = 60
         * Total = 60
         * Penalty = 3600
         */

        constraintVerifier
                .verifyThat(TripExecutionConstraintProvider::fairnessConductorDuty)
                .given(planning)
                .penalizesBy(3600);
    }
}
