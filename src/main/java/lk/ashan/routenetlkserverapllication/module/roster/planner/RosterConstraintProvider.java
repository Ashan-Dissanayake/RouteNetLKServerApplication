package lk.ashan.routenetlkserverapllication.module.roster.planner;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.*;
import org.springframework.stereotype.Component;

@Component
public class RosterConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                // Hard Constraints - Must be satisfied
                designationMatch(factory),
                sameBranch(factory),
                noDoubleBooking(factory),
                validMedicalCertificate(factory),
                validDriverLicense(factory),
                eligibleCrewOnly(factory),

                // Soft Constraints - Optimization goals
                preferRouteFamiliarity(factory),
                balanceWorkload(factory)
        };
    }

    // ============================================
    // HARD CONSTRAINTS
    // ============================================

    /**
     * HC1: Employee designation must match required role (driver/conductor)
     */
    Constraint designationMatch(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .filter(a -> !a.getEmployee().matchesDesignation(a.getRequiredDesignationId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Designation must match required role");
    }

    /**
     * HC2: Employee must belong to the same branch as the roster
     */
    Constraint sameBranch(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .filter(a -> !a.getEmployee().belongsToBranch(a.getBranchId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Employee must belong to same branch");
    }

    /**
     * HC3: Employee cannot be assigned to multiple rosters on the same date and shift
     */
    Constraint noDoubleBooking(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .groupBy(
                        RosterAssignmentPlanning::getEmployee,
                        RosterAssignmentPlanning::getRosterDate,
                        RosterAssignmentPlanning::getShiftId,
                        ConstraintCollectors.count()
                )
                .filter((employee, date, shift, count) -> count > 1)
                .penalize(HardSoftScore.ONE_HARD,
                        (employee, date, shift, count) -> count - 1)
                .asConstraint("Employee cannot be double-booked");
    }

    /**
     * HC4: Employee must have a valid medical certificate
     */
    Constraint validMedicalCertificate(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .filter(a -> !a.getEmployee().hasValidMedical(a.getRosterDate()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Valid medical certificate required");
    }

    /**
     * HC5: Driver must have a valid license for the roster date
     */
    Constraint validDriverLicense(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .filter(RosterAssignmentPlanning::isDriverSlot)
                .filter(a -> !a.getEmployee().hasValidLicense(a.getRosterDate()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Valid driver license required");
    }

    /**
     * HC6: Only eligible crew (status = Eligible or Active) can be assigned
     */
    Constraint eligibleCrewOnly(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .filter(a -> !a.getEmployee().isEligible())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Only eligible crew can be assigned");
    }

    // ============================================
    // SOFT CONSTRAINTS
    // ============================================

    /**
     * SC1: Prefer employees with higher route familiarity
     * Higher familiarity level = better score
     */
    Constraint preferRouteFamiliarity(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .filter(a -> a.getEmployee().getRouteFamiliarityLevelId() != null)
                .reward(HardSoftScore.ONE_SOFT,
                        a -> a.getEmployee().getRouteFamiliarityLevelId())
                .asConstraint("Prefer higher route familiarity");
    }

    /**
     * SC2: Balance workload - prefer even distribution of assignments
     * Penalize employees who get too many assignments
     */
    private Constraint balanceWorkload(ConstraintFactory factory) {
        return factory.forEach(RosterAssignmentPlanning.class)
                .filter(RosterAssignmentPlanning::hasEmployee)
                .groupBy(
                        RosterAssignmentPlanning::getEmployee,
                        ConstraintCollectors.count()
                )
                .filter((employee, count) -> count > 1)
                .penalize(HardSoftScore.ONE_SOFT,
                        (employee, count) -> (count - 1) * (count - 1)) // Quadratic penalty
                .asConstraint("Balance workload evenly");
    }

}
