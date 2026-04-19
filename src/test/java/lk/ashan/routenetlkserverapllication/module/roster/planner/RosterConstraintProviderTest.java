package lk.ashan.routenetlkserverapllication.module.roster.planner;

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;


class RosterConstraintProviderTest {

    ConstraintVerifier<RosterConstraintProvider, RosterShiftAssignmentSolution> verifier =
            ConstraintVerifier.build(new RosterConstraintProvider(),
                    RosterShiftAssignmentSolution.class,
                    RosterShiftAssignmentPlanning.class);


    @Test
    void requiredDesignation_shouldPenalizeMismatch() {
        // 1. Setup Data (Driver assigned to a Conductor shift)
        EmployeeFact driver = new EmployeeFact(1, "Saman Kumara", 1, 1);
        RosterShiftAssignmentPlanning invalidAssignment = new RosterShiftAssignmentPlanning();
        invalidAssignment.setId(101);
        invalidAssignment.setDesignationId(2); // 2 = Conductor Shift
        invalidAssignment.setEmployeeFact(driver);

        // 2. Verify: Should penalize by 1 Hard score
        verifier.verifyThat(RosterConstraintProvider::requiredDesignation)
                .given(invalidAssignment)
                .penalizesBy(1);
    }

    @Test
    void noOverlappingShifts_shouldPenalizeOverlap() {
        EmployeeFact driver = new EmployeeFact(1, "Saman Kumara", 1, 1);        LocalDate today = LocalDate.now();

        // Shift A
        RosterShiftAssignmentPlanning shiftA = new RosterShiftAssignmentPlanning();
        shiftA.setId(1); // <--- CRITICAL: Set unique ID
        shiftA.setEmployeeFact(driver);
        shiftA.setShiftDate(today);
        shiftA.setStartTime(LocalTime.of(8, 0));
        shiftA.setEndTime(LocalTime.of(12, 0));

        // Shift B
        RosterShiftAssignmentPlanning shiftB = new RosterShiftAssignmentPlanning();
        shiftB.setId(2); // <--- CRITICAL: Set DIFFERENT unique ID
        shiftB.setEmployeeFact(driver);
        shiftB.setShiftDate(today);
        shiftB.setStartTime(LocalTime.of(11, 0));
        shiftB.setEndTime(LocalTime.of(15, 0));

        verifier.verifyThat(RosterConstraintProvider::noOverlappingShifts)
                .given(shiftA, shiftB)
                .penalizesBy(1);
    }

    @Test
    void noOverlappingShifts_shouldNotPenalizeValidSequence() {
        EmployeeFact driver = new EmployeeFact(1, "Saman Kumara", 1, 1);        LocalDate today = LocalDate.now();

        RosterShiftAssignmentPlanning morningShift = new RosterShiftAssignmentPlanning();
        morningShift.setId(3); // <--- Unique ID
        morningShift.setEmployeeFact(driver);
        morningShift.setShiftDate(today);
        morningShift.setStartTime(LocalTime.of(6, 0));
        morningShift.setEndTime(LocalTime.of(10, 0));

        RosterShiftAssignmentPlanning eveningShift = new RosterShiftAssignmentPlanning();
        eveningShift.setId(4); // <--- Unique ID
        eveningShift.setEmployeeFact(driver);
        eveningShift.setShiftDate(today);
        eveningShift.setStartTime(LocalTime.of(14, 0));
        eveningShift.setEndTime(LocalTime.of(18, 0));

        verifier.verifyThat(RosterConstraintProvider::noOverlappingShifts)
                .given(morningShift, eveningShift)
                .penalizesBy(0);
    }


    @Test
    void routeFamiliarityMatch_shouldPenalizeInexperiencedCrew() {
        // 1. Setup: A Driver with LOW familiarity (1)
        EmployeeFact juniorDriver = new EmployeeFact(1, "Sunil", 1, 1);

        // 2. Setup: A Planning Shift that requires HIGH familiarity (2)
        // This simulates a shift that covers an Interprovincial trip
        RosterShiftAssignmentPlanning interprovincialAssignment = new RosterShiftAssignmentPlanning();
        interprovincialAssignment.setId(10);
        interprovincialAssignment.setEmployeeFact(juniorDriver);
        interprovincialAssignment.setRequiredFamiliarityLevel(2); // Set via our Pre-Processor

        // 3. Verify: Penalize because 1 < 2
        verifier.verifyThat(RosterConstraintProvider::routeFamiliarityMatchConstraint)
                .given(interprovincialAssignment)
                .penalizesBy(1);
    }

    @Test
    void routeFamiliarityMatch_shouldNotPenalizeExperiencedCrew() {
        // 1. Setup: A Driver with HIGH familiarity (2)
        EmployeeFact seniorDriver = new EmployeeFact(2, "Nimal", 1, 2);

        RosterShiftAssignmentPlanning interprovincialAssignment = new RosterShiftAssignmentPlanning();
        interprovincialAssignment.setId(11);
        interprovincialAssignment.setEmployeeFact(seniorDriver);
        interprovincialAssignment.setRequiredFamiliarityLevel(2);

        // 2. Verify: Should NOT penalize because 2 >= 2
        verifier.verifyThat(RosterConstraintProvider::routeFamiliarityMatchConstraint)
                .given(interprovincialAssignment)
                .penalizesBy(0);
    }

    @Test
    void oneDriverOneConductor_shouldPenalizeTwoDrivers() {
        EmployeeFact driverA = new EmployeeFact(1, "Saman", 1, 1); // 1 = Driver
        EmployeeFact driverB = new EmployeeFact(2, "Kamal", 1, 1); // 1 = Driver

        // Two assignments for the SAME physical shift ID
        RosterShiftAssignmentPlanning slot1 = new RosterShiftAssignmentPlanning();
        slot1.setId(50);
        slot1.setShiftId(500); // Same Shift
        slot1.setEmployeeFact(driverA);

        RosterShiftAssignmentPlanning slot2 = new RosterShiftAssignmentPlanning();
        slot2.setId(51);
        slot2.setShiftId(500); // Same Shift
        slot2.setEmployeeFact(driverB);

        verifier.verifyThat(RosterConstraintProvider::oneDriverOneConductorPerShift)
                .given(slot1, slot2)
                .penalizesBy(1);
    }
}
