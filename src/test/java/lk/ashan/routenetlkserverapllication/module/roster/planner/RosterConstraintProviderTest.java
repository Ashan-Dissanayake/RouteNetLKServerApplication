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

    // --- HARD CONSTRAINTS ---
    @Test
    void designationMatch_shouldPenalizeMismatch() {
        // Driver (1) assigned to a Conductor slot (2)
        EmployeeFact driver = new EmployeeFact(1, "Saman Kumara", 1, 1);

        RosterShiftAssignmentPlanning slot = new RosterShiftAssignmentPlanning();
        slot.setId(101);
        slot.setDesignationId(2); // Needs Conductor
        slot.setEmployeeFact(driver);

        verifier.verifyThat(RosterConstraintProvider::designationMatch)
                .given(slot)
                .penalizesBy(1);
    }

    @Test
    void noOverlappingShifts_shouldPenalizeOverlap() {
        EmployeeFact driver = new EmployeeFact(1, "Saman Kumara", 1, 1);
        LocalDate today = LocalDate.now();

        RosterShiftAssignmentPlanning shiftA = new RosterShiftAssignmentPlanning();
        shiftA.setId(1);
        shiftA.setEmployeeFact(driver);
        shiftA.setShiftDate(today);
        shiftA.setStartTime(LocalTime.of(8, 0));
        shiftA.setEndTime(LocalTime.of(12, 0));

        RosterShiftAssignmentPlanning shiftB = new RosterShiftAssignmentPlanning();
        shiftB.setId(2);
        shiftB.setEmployeeFact(driver);
        shiftB.setShiftDate(today);
        shiftB.setStartTime(LocalTime.of(11, 0)); // Overlaps with Shift A
        shiftB.setEndTime(LocalTime.of(15, 0));

        verifier.verifyThat(RosterConstraintProvider::noOverlappingShifts)
                .given(shiftA, shiftB)
                .penalizesBy(1);
    }

    @Test
    void oneDriverOneConductorPerShift_shouldPenalizeSameDesignations() {
        // Two Conductors (Designation 2)
        EmployeeFact conductorA = new EmployeeFact(3, "Anura", 2, 1);
        EmployeeFact conductorB = new EmployeeFact(4, "Bimal", 2, 1);

        // Assigned to the SAME shiftId (The same physical bus trip)
        RosterShiftAssignmentPlanning slot1 = new RosterShiftAssignmentPlanning();
        slot1.setId(50);
        slot1.setShiftId(500);
        slot1.setEmployeeFact(conductorA);

        RosterShiftAssignmentPlanning slot2 = new RosterShiftAssignmentPlanning();
        slot2.setId(51);
        slot2.setShiftId(500);
        slot2.setEmployeeFact(conductorB);

        verifier.verifyThat(RosterConstraintProvider::oneDriverOneConductorPerShift)
                .given(slot1, slot2)
                .penalizesBy(1);
    }

    @Test
    void routeFamiliarityMatchConstraint_shouldPenalizeLowSkill() {
        // Level 1 (Local) employee assigned to Level 2 (Interprovincial) shift
        EmployeeFact junior = new EmployeeFact(1, "Sunil", 1, 1);

        RosterShiftAssignmentPlanning highSkillSlot = new RosterShiftAssignmentPlanning();
        highSkillSlot.setId(10);
        highSkillSlot.setEmployeeFact(junior);
        highSkillSlot.setRequiredFamiliarityLevel(2);

        verifier.verifyThat(RosterConstraintProvider::routeFamiliarityMatchConstraint)
                .given(highSkillSlot)
                .penalizesBy(1);
    }

    @Test
    void routeFamiliarityMatchConstraint_shouldNotPenalizeHighSkill() {
        // Level 2 employee assigned to Level 2 shift
        EmployeeFact senior = new EmployeeFact(2, "Nimal", 1, 2);

        RosterShiftAssignmentPlanning highSkillSlot = new RosterShiftAssignmentPlanning();
        highSkillSlot.setId(11);
        highSkillSlot.setEmployeeFact(senior);
        highSkillSlot.setRequiredFamiliarityLevel(2);

        verifier.verifyThat(RosterConstraintProvider::routeFamiliarityMatchConstraint)
                .given(highSkillSlot)
                .penalizesBy(0);
    }

    // --- SOFT CONSTRAINTS ---

    @Test
    void fairWorkloadDistribution_shouldPenalizeUnbalancedWorkload() {
        EmployeeFact driver = new EmployeeFact(1, "Saman", 1, 1);

        // Assigning 2 shifts to the same driver
        RosterShiftAssignmentPlanning assignment1 = new RosterShiftAssignmentPlanning();
        assignment1.setId(1);
        assignment1.setEmployeeFact(driver);

        RosterShiftAssignmentPlanning assignment2 = new RosterShiftAssignmentPlanning();
        assignment2.setId(2);
        assignment2.setEmployeeFact(driver);

        // Workload distribution uses count^2 as penalty
        // 2 shifts = 2*2 = 4 penalty
        verifier.verifyThat(RosterConstraintProvider::fairWorkloadDistribution)
                .given(assignment1, assignment2)
                .penalizesBy(4);
    }
}
