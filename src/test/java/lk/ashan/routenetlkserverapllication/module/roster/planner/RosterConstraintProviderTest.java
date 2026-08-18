package lk.ashan.routenetlkserverapllication.module.roster.planner;


import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

class RosterConstraintProviderTest {

    ConstraintVerifier<RosterConstraintProvider, RosterShiftAssignmentSolution> constraintVerifier =
            ConstraintVerifier.build(new RosterConstraintProvider(),
                    RosterShiftAssignmentSolution.class,
                    RosterShiftAssignmentPlanning.class);

    private EmployeeFact driver;
    private EmployeeFact conductor;

    private final LocalDate shiftDate = LocalDate.of(2026, 8, 18);

    @BeforeEach
    void setUp() {

        constraintVerifier = ConstraintVerifier.build(
                new RosterConstraintProvider(),
                RosterShiftAssignmentSolution.class,
                RosterShiftAssignmentPlanning.class
        );

        driver = new EmployeeFact(
                1001,
                "Test Driver",
                1,
                3
        );

        conductor = new EmployeeFact(
                1002,
                "Test Conductor",
                2,
                3
        );
    }


    // ============================================================
    // requiredDesignation()
    // ============================================================

    @Test
    void requiredDesignation_shouldPenalize_whenEmployeeDesignationDoesNotMatch() {

        RosterShiftAssignmentPlanning assignment =
                assignment(
                        1,
                        101,
                        1,
                        1,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        // Required designation = conductor (2)
        assignment.setDesignationId(2);

        constraintVerifier
                .verifyThat(RosterConstraintProvider::requiredDesignation)
                .given(assignment)
                .penalizesBy(1);
    }


    @Test
    void requiredDesignation_shouldNotPenalize_whenEmployeeDesignationMatches() {

        RosterShiftAssignmentPlanning assignment =
                assignment(
                        1,
                        101,
                        1,
                        1,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        // Required designation = driver (1)
        assignment.setDesignationId(1);

        constraintVerifier
                .verifyThat(RosterConstraintProvider::requiredDesignation)
                .given(assignment)
                .penalizesBy(0);
    }


    @Test
    void requiredDesignation_shouldNotPenalize_whenEmployeeIsUnassigned() {

        RosterShiftAssignmentPlanning assignment =
                assignment(
                        1,
                        101,
                        1,
                        1,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        null
                );

        assignment.setDesignationId(1);

        constraintVerifier
                .verifyThat(RosterConstraintProvider::requiredDesignation)
                .given(assignment)
                .penalizesBy(0);
    }


    // ============================================================
    // designationMatch()
    // ============================================================

    @Test
    void designationMatch_shouldPenalize_whenDesignationDoesNotMatch() {

        RosterShiftAssignmentPlanning assignment =
                assignment(
                        2,
                        102,
                        1,
                        2,
                        LocalTime.of(9, 0),
                        LocalTime.of(13, 0),
                        driver
                );

        // Required designation = conductor
        assignment.setDesignationId(2);

        constraintVerifier
                .verifyThat(RosterConstraintProvider::designationMatch)
                .given(assignment)
                .penalizesBy(1);
    }


    @Test
    void designationMatch_shouldNotPenalize_whenDesignationMatches() {

        RosterShiftAssignmentPlanning assignment =
                assignment(
                        3,
                        103,
                        1,
                        3,
                        LocalTime.of(9, 0),
                        LocalTime.of(13, 0),
                        driver
                );

        assignment.setDesignationId(1);

        constraintVerifier
                .verifyThat(RosterConstraintProvider::designationMatch)
                .given(assignment)
                .penalizesBy(0);
    }


    // ============================================================
    // noOverlappingShifts()
    // ============================================================

    @Test
    void noOverlappingShifts_shouldPenalize_whenSameEmployeeHasOverlappingShifts() {

        RosterShiftAssignmentPlanning assignment1 =
                assignment(
                        10,
                        201,
                        1,
                        10,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        RosterShiftAssignmentPlanning assignment2 =
                assignment(
                        11,
                        202,
                        2,
                        10,
                        LocalTime.of(10, 0),
                        LocalTime.of(14, 0),
                        driver
                );

        constraintVerifier
                .verifyThat(RosterConstraintProvider::noOverlappingShifts)
                .given(assignment1, assignment2)
                .penalizesBy(1);
    }


    @Test
    void noOverlappingShifts_shouldNotPenalize_whenSameEmployeeHasNonOverlappingShifts() {

        RosterShiftAssignmentPlanning assignment1 =
                assignment(
                        12,
                        203,
                        1,
                        11,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        RosterShiftAssignmentPlanning assignment2 =
                assignment(
                        13,
                        204,
                        2,
                        11,
                        LocalTime.of(12, 0),
                        LocalTime.of(16, 0),
                        driver
                );

        constraintVerifier
                .verifyThat(RosterConstraintProvider::noOverlappingShifts)
                .given(assignment1, assignment2)
                .penalizesBy(0);
    }


    @Test
    void noOverlappingShifts_shouldNotPenalize_whenDifferentEmployeesHaveOverlappingShifts() {

        RosterShiftAssignmentPlanning assignment1 =
                assignment(
                        14,
                        205,
                        1,
                        12,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        RosterShiftAssignmentPlanning assignment2 =
                assignment(
                        15,
                        206,
                        2,
                        12,
                        LocalTime.of(10, 0),
                        LocalTime.of(14, 0),
                        conductor
                );

        constraintVerifier
                .verifyThat(RosterConstraintProvider::noOverlappingShifts)
                .given(assignment1, assignment2)
                .penalizesBy(0);
    }


    // ============================================================
    // oneDriverOneConductorPerShift()
    // ============================================================

    @Test
    void oneDriverOneConductorPerShift_shouldPenalize_whenSameShiftHasTwoDrivers() {

        EmployeeFact secondDriver = new EmployeeFact(
                1003,
                "Second Driver",
                1,
                3
        );

        RosterShiftAssignmentPlanning assignment1 =
                assignment(
                        20,
                        301,
                        1,
                        20,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        RosterShiftAssignmentPlanning assignment2 =
                assignment(
                        21,
                        302,
                        2,
                        20,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        secondDriver
                );

        constraintVerifier
                .verifyThat(RosterConstraintProvider::oneDriverOneConductorPerShift)
                .given(assignment1, assignment2)
                .penalizesBy(1);
    }


    @Test
    void oneDriverOneConductorPerShift_shouldPenalize_whenSameShiftHasTwoConductors() {

        EmployeeFact secondConductor = new EmployeeFact(
                1004,
                "Second Conductor",
                2,
                3
        );

        RosterShiftAssignmentPlanning assignment1 =
                assignment(
                        22,
                        303,
                        1,
                        21,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        conductor
                );

        RosterShiftAssignmentPlanning assignment2 =
                assignment(
                        23,
                        304,
                        2,
                        21,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        secondConductor
                );

        constraintVerifier
                .verifyThat(RosterConstraintProvider::oneDriverOneConductorPerShift)
                .given(assignment1, assignment2)
                .penalizesBy(1);
    }


    @Test
    void oneDriverOneConductorPerShift_shouldNotPenalize_whenShiftHasDriverAndConductor() {

        RosterShiftAssignmentPlanning assignment1 =
                assignment(
                        24,
                        305,
                        1,
                        22,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        RosterShiftAssignmentPlanning assignment2 =
                assignment(
                        25,
                        306,
                        2,
                        22,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        conductor
                );

        constraintVerifier
                .verifyThat(RosterConstraintProvider::oneDriverOneConductorPerShift)
                .given(assignment1, assignment2)
                .penalizesBy(0);
    }


    // ============================================================
    // fairWorkloadDistribution()
    // ============================================================

    @Test
    void fairWorkloadDistribution_shouldPenalizeWorkloadForEmployee() {

        RosterShiftAssignmentPlanning assignment1 =
                assignment(
                        30,
                        401,
                        1,
                        30,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        RosterShiftAssignmentPlanning assignment2 =
                assignment(
                        31,
                        402,
                        2,
                        31,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0),
                        driver
                );

        constraintVerifier
                .verifyThat(RosterConstraintProvider::fairWorkloadDistribution)
                .given(assignment1, assignment2)
                .penalizesBy(4);
    }

    @Test
    void fairWorkloadDistribution_shouldNotPenalize_whenEmployeeHasNoAssignments() {

        constraintVerifier
                .verifyThat(RosterConstraintProvider::fairWorkloadDistribution)
                .given()
                .penalizesBy(0);
    }


    // ============================================================
    // Helper
    // ============================================================

    private RosterShiftAssignmentPlanning assignment(
            Integer id,
            Integer rosterShiftId,
            Integer designationId,
            Integer shiftId,
            LocalTime startTime,
            LocalTime endTime,
            EmployeeFact employeeFact) {

        RosterShiftAssignmentPlanning assignment =
                new RosterShiftAssignmentPlanning();

        assignment.setId(id);
        assignment.setRosterShiftId(rosterShiftId);
        assignment.setDesignationId(designationId);
        assignment.setShiftId(shiftId);
        assignment.setStartTime(startTime);
        assignment.setEndTime(endTime);
        assignment.setShiftDate(shiftDate);
        assignment.setEmployeeFact(employeeFact);
        assignment.setRequiredFamiliarityLevel(1);

        return assignment;
    }
}
