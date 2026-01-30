package lk.ashan.routenetlkserverapllication.module.roster.planner;

import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

import java.time.LocalDate;


public class RosterConstraintProviderTest {

    private final ConstraintVerifier<RosterConstraintProvider, RosterAssignmentSolution> constraintVerifier =
            ConstraintVerifier.build(
                    new RosterConstraintProvider(),
                    RosterAssignmentSolution.class,
                    RosterAssignmentPlanning.class
            );

    @Test
    void testDesignationMatch_WhenMatches_NoPenalty() {
        // Create driver employee
        EmployeePlanning driver = EmployeePlanning.builder()
                .id(1)
                .designationId(1) // Driver
                .isDriver(true)
                .build();

        // Create driver slot
        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-1");
        assignment.setRequiredDesignationId(1); // Driver slot
        assignment.setEmployee(driver);

        // Verify no penalty when designation matches
        constraintVerifier.verifyThat(RosterConstraintProvider::designationMatch)
                .given(assignment)
                .penalizesBy(0);
    }

    @Test
    void testDesignationMatch_WhenMismatched_Penalizes() {
        // Create conductor employee
        EmployeePlanning conductor = EmployeePlanning.builder()
                .id(2)
                .designationId(2) // Conductor
                .isConductor(true)
                .build();

        // Try to assign conductor to driver slot
        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-2");
        assignment.setRequiredDesignationId(1); // Driver slot
        assignment.setEmployee(conductor);

        // Verify penalty when designation doesn't match
        constraintVerifier.verifyThat(RosterConstraintProvider::designationMatch)
                .given(assignment)
                .penalizesBy(1);
    }

    @Test
    void testSameBranch_WhenMatches_NoPenalty() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .branchId(1)
                .designationId(1)
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-3");
        assignment.setBranchId(1); // Same branch
        assignment.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::sameBranch)
                .given(assignment)
                .penalizesBy(0);
    }

    @Test
    void testSameBranch_WhenDifferent_Penalizes() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .branchId(1)
                .designationId(1)
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-4");
        assignment.setBranchId(2); // Different branch
        assignment.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::sameBranch)
                .given(assignment)
                .penalizesBy(1);
    }

    @Test
    void testNoDoubleBooking_WhenSingleAssignment_NoPenalty() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-5");
        assignment.setRosterDate(LocalDate.of(2026, 2, 1));
        assignment.setShiftId("1");
        assignment.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::noDoubleBooking)
                .given(assignment)
                .penalizesBy(0);
    }

    @Test
    void testNoDoubleBooking_WhenDoubleBooked_Penalizes() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .build();

        // Same employee assigned to two rosters on same date and shift
        RosterAssignmentPlanning assignment1 = new RosterAssignmentPlanning();
        assignment1.setId("test-6a");
        assignment1.setRosterDate(LocalDate.of(2026, 2, 1));
        assignment1.setShiftId("1");
        assignment1.setEmployee(employee);

        RosterAssignmentPlanning assignment2 = new RosterAssignmentPlanning();
        assignment2.setId("test-6b");
        assignment2.setRosterDate(LocalDate.of(2026, 2, 1));
        assignment2.setShiftId("1");
        assignment2.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::noDoubleBooking)
                .given(assignment1, assignment2)
                .penalizesBy(1); // One violation (2 assignments - 1)
    }

    @Test
    void testValidMedicalCertificate_WhenValid_NoPenalty() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .medicalExpiry(LocalDate.of(2026, 12, 31)) // Future date
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-7");
        assignment.setRosterDate(LocalDate.of(2026, 2, 1));
        assignment.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::validMedicalCertificate)
                .given(assignment)
                .penalizesBy(0);
    }

    @Test
    void testValidMedicalCertificate_WhenExpired_Penalizes() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .medicalExpiry(LocalDate.of(2025, 12, 31)) // Past date
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-8");
        assignment.setRosterDate(LocalDate.of(2026, 2, 1));
        assignment.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::validMedicalCertificate)
                .given(assignment)
                .penalizesBy(1);
    }

    @Test
    void testValidDriverLicense_WhenValid_NoPenalty() {
        EmployeePlanning driver = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .isDriver(true)
                .licenseExpiry(LocalDate.of(2026, 12, 31)) // Future date
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-9");
        assignment.setRequiredDesignationId(1); // Driver slot
        assignment.setRosterDate(LocalDate.of(2026, 2, 1));
        assignment.setEmployee(driver);

        constraintVerifier.verifyThat(RosterConstraintProvider::validDriverLicense)
                .given(assignment)
                .penalizesBy(0);
    }

    @Test
    void testValidDriverLicense_WhenExpired_Penalizes() {
        EmployeePlanning driver = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .isDriver(true)
                .licenseExpiry(LocalDate.of(2025, 12, 31)) // Past date
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-10");
        assignment.setRequiredDesignationId(1); // Driver slot
        assignment.setRosterDate(LocalDate.of(2026, 2, 1));
        assignment.setEmployee(driver);

        constraintVerifier.verifyThat(RosterConstraintProvider::validDriverLicense)
                .given(assignment)
                .penalizesBy(1);
    }

    @Test
    void testEligibleCrewOnly_WhenEligible_NoPenalty() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .crewStatusId(1) // Eligible
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-11");
        assignment.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::eligibleCrewOnly)
                .given(assignment)
                .penalizesBy(0);
    }

    @Test
    void testEligibleCrewOnly_WhenIneligible_Penalizes() {
        EmployeePlanning employee = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .crewStatusId(2) // Ineligible
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-12");
        assignment.setEmployee(employee);

        constraintVerifier.verifyThat(RosterConstraintProvider::eligibleCrewOnly)
                .given(assignment)
                .penalizesBy(1);
    }

    @Test
    void testPreferRouteFamiliarity_RewardsHigherFamiliarity() {
        // Employee with high familiarity
        EmployeePlanning highFamiliarityEmployee = EmployeePlanning.builder()
                .id(1)
                .designationId(1)
                .routeFamiliarityLevelId(3) // High = 3
                .build();

        RosterAssignmentPlanning assignment = new RosterAssignmentPlanning();
        assignment.setId("test-13");
        assignment.setEmployee(highFamiliarityEmployee);

        constraintVerifier.verifyThat(RosterConstraintProvider::preferRouteFamiliarity)
                .given(assignment)
                .rewardsWith(3); // Should reward by familiarity level
    }
}
