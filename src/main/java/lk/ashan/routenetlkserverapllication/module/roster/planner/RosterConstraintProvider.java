package lk.ashan.routenetlkserverapllication.module.roster.planner;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.*;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;

import java.util.Objects;

/**
 * Provides constraints for the roster planning problem.
 * Implements the {@link ConstraintProvider} interface to define both hard and soft constraints.
 */
public class RosterConstraintProvider implements ConstraintProvider {

    /**
     * Defines the constraints for the roster planning problem.
     *
     * @param factory the {@link ConstraintFactory} used to create constraints.
     * @return an array of {@link Constraint} objects representing the defined constraints.
     */
    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                // Hard Constraints (Must be met)
                requiredDesignation(factory),
                noOverlappingShifts(factory),
                designationMatch(factory),
                oneDriverOneConductorPerShift(factory),
                //routeFamiliarityMatchConstraint(factory),

                // Soft Constraints (Preferences)
                fairWorkloadDistribution(factory)
        };
    }

    /**
     * Ensures that employees have the correct designation (e.g., Driver or Conductor) for their assigned shifts.
     *
     * @param factory the {@link ConstraintFactory} used to create the constraint.
     * @return a {@link Constraint} penalizing assignments with mismatched designations.
     */
    Constraint requiredDesignation(ConstraintFactory factory) {
        return factory.forEach(RosterShiftAssignmentPlanning.class)
                .filter(assignment -> assignment.getEmployeeFact() != null)
                .filter(assignment -> !Objects.equals(
                        assignment.getEmployeeFact().getDesignationId(),
                        assignment.getDesignationId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Designation Mismatch");
    }

    /**
     * Prevents employees from being assigned to overlapping shifts.
     *
     * @param factory the {@link ConstraintFactory} used to create the constraint.
     * @return a {@link Constraint} penalizing overlapping shift assignments for the same employee.
     */
    Constraint noOverlappingShifts(ConstraintFactory factory) {
        return factory.forEachUniquePair(RosterShiftAssignmentPlanning.class,
                        Joiners.equal(RosterShiftAssignmentPlanning::getEmployeeFact),
                        Joiners.equal(RosterShiftAssignmentPlanning::getShiftDate),
                        Joiners.overlapping(RosterShiftAssignmentPlanning::getStartTime,
                                RosterShiftAssignmentPlanning::getEndTime))
                .filter((a1, a2) -> a1.getEmployeeFact() != null)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Overlapping Shifts");
    }

    /**
     * Ensures that the employee's designation matches the required designation for the shift.
     *
     * @param factory the {@link ConstraintFactory} used to create the constraint.
     * @return a {@link Constraint} penalizing mismatched designations.
     */
    public Constraint designationMatch(ConstraintFactory factory) {
        return factory.forEach(RosterShiftAssignmentPlanning.class)
                .filter(assignment -> assignment.getEmployeeFact() != null &&
                        !assignment.getEmployeeFact().getDesignationId().equals(assignment.getDesignationId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Designation Match");
    }

    /**
     * Ensures that each shift has one driver and one conductor, preventing duplicate designations.
     *
     * @param factory the {@link ConstraintFactory} used to create the constraint.
     * @return a {@link Constraint} penalizing shifts with duplicate designations.
     */
    public Constraint oneDriverOneConductorPerShift(ConstraintFactory factory) {
        return factory.forEachUniquePair(RosterShiftAssignmentPlanning.class,
                        Joiners.equal(RosterShiftAssignmentPlanning::getShiftId))
                .filter((a1, a2) -> a1.getEmployeeFact().getDesignationId()
                        .equals(a2.getEmployeeFact().getDesignationId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Shift must have unique designations");
    }

    /**
     * Ensures that employees assigned to shifts have the required familiarity level for the route.
     *
     * @param constraintFactory the {@link ConstraintFactory} used to create the constraint.
     * @return a {@link Constraint} penalizing assignments where employees lack the required familiarity.
     */
    public Constraint routeFamiliarityMatchConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(RosterShiftAssignmentPlanning.class)
                .filter(assignment -> assignment.getEmployeeFact() != null)
                .filter(assignment -> !assignment.getEmployeeFact()
                        .hasRequiredFamiliarity(assignment.getRequiredFamiliarityLevel()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Familiarity Mismatch");
    }

    /**
     * Encourages fair workload distribution by penalizing uneven shift assignments.
     *
     * @param factory the {@link ConstraintFactory} used to create the constraint.
     * @return a {@link Constraint} penalizing uneven workload distribution.
     */
    Constraint fairWorkloadDistribution(ConstraintFactory factory) {
        return factory.forEach(RosterShiftAssignmentPlanning.class)
                .groupBy(RosterShiftAssignmentPlanning::getEmployeeFact, ConstraintCollectors.count())
                .penalize(HardSoftScore.ONE_SOFT, (employee, count) -> count * count)
                .asConstraint("Fair workload");
    }

}
