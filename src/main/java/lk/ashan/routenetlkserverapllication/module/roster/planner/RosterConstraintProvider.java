package lk.ashan.routenetlkserverapllication.module.roster.planner;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.*;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;

import java.util.Objects;

public class RosterConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                // Hard Constraints (Must be met)
                requiredDesignation(factory),
                noOverlappingShifts(factory),
                designationMatch(factory),
                oneDriverOneConductorPerShift(factory),
                routeFamiliarityMatchConstraint(factory),

                // Soft Constraints (Preferences)
                fairWorkloadDistribution(factory)
        };
    }

    // 1. HARD: Employee must have the correct designation (Driver/Conductor)
    Constraint requiredDesignation(ConstraintFactory factory) {
        return factory.forEach(RosterShiftAssignmentPlanning.class)
                // Only check if an employee has actually been assigned to this slot
                .filter(assignment -> assignment.getEmployeeFact() != null)
                .filter(assignment -> !Objects.equals(
                        assignment.getEmployeeFact().getDesignationId(),
                        assignment.getDesignationId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Designation Mismatch");
    }
    // 2. HARD: An employee cannot be in two places at once
    Constraint noOverlappingShifts(ConstraintFactory factory) {
        return factory.forEachUniquePair(RosterShiftAssignmentPlanning.class,
                        Joiners.equal(RosterShiftAssignmentPlanning::getEmployeeFact),
                        Joiners.equal(RosterShiftAssignmentPlanning::getShiftDate),
                        Joiners.overlapping(RosterShiftAssignmentPlanning::getStartTime,
                                RosterShiftAssignmentPlanning::getEndTime))
                // Only penalize if the same person is assigned to both overlapping slots
                .filter((a1, a2) -> a1.getEmployeeFact() != null)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Overlapping Shifts");
    }

    public Constraint designationMatch(ConstraintFactory factory) {
        return factory.forEach(RosterShiftAssignmentPlanning.class)
                .filter(assignment -> assignment.getEmployeeFact() != null &&
                        !assignment.getEmployeeFact().getDesignationId().equals(assignment.getDesignationId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Designation Match");
    }


    public Constraint oneDriverOneConductorPerShift(ConstraintFactory factory) {
        return factory.forEachUniquePair(RosterShiftAssignmentPlanning.class,
                        // Join on the same physical bus shift
                        Joiners.equal(RosterShiftAssignmentPlanning::getShiftId))
                .filter((a1, a2) -> {
                    // If they are both the same designation, it's a violation
                    return a1.getEmployeeFact().getDesignationId()
                            .equals(a2.getEmployeeFact().getDesignationId());
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Shift must have unique designations");
    }

    public Constraint routeFamiliarityMatchConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(RosterShiftAssignmentPlanning.class)
                .filter(assignment -> assignment.getEmployeeFact() != null)
                // Use your new helper method here
                .filter(assignment -> !assignment.getEmployeeFact()
                        .hasRequiredFamiliarity(assignment.getRequiredFamiliarityLevel()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Familiarity Mismatch");
    }

    // 3. SOFT: Fairness - try to distribute shifts evenly (Simplified)
    Constraint fairWorkloadDistribution(ConstraintFactory factory) {
        return factory.forEach(RosterShiftAssignmentPlanning.class)
                .groupBy(RosterShiftAssignmentPlanning::getEmployeeFact, ConstraintCollectors.count())
                .penalize(HardSoftScore.ONE_SOFT, (employee, count) -> count * count)
                .asConstraint("Fair workload");
    }

}
