package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.planner;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.Tripcrewallocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

/**
 * Planning Solution for Trip Crew Allocation.
 *
 * This class represents the complete problem that OptaPlanner will solve.
 * It contains:
 * - Planning entities: Assignments to optimize (which employee to assign to which role)
 * - Problem facts: Immutable data used in constraints (employees, rosters, existing allocations)
 * - Score: How good the solution is (calculated by constraint provider)
 *
 * OptaPlanner Flow:
 * 1. Start with unassigned entities (assignedEmployee = null)
 * 2. Try different employee assignments
 * 3. Calculate score for each attempt
 * 4. Keep trying to improve score
 * 5. Return best solution found
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningSolution
public class TripCrewScheduleSolution {

    /**
     * List of assignments to optimize.
     * These are the variables OptaPlanner will change.
     *
     * Example: For 1 trip needing driver + conductor = 2 assignments
     *          For 10 trips = 20 assignments
     */
    @PlanningEntityCollectionProperty
    private List<TripCrewAssignmentPlanning> assignments;

    /**
     * Available employees that can be assigned.
     * This provides the "value range" for the planning variable.
     *
     * Only includes employees from confirmed roster assignments.
     */
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "employeeRange")
    private List<EmployeeFact> employeeList;

    /**
     * Confirmed roster assignments for the shift/date.
     * Used by constraints to validate that assigned employee has confirmed roster.
     *
     * Problem fact = immutable reference data.
     */
    @ProblemFactCollectionProperty
    private List<Shiftrosterassignment> confirmedRosterAssignments;

    /**
     * Existing crew allocations (already assigned to other trips).
     * Used to prevent double-booking in constraints.
     *
     * Problem fact = immutable reference data.
     */
    @ProblemFactCollectionProperty
    private List<Tripcrewallocation> existingAllocations;

    /**
     * The score of this solution.
     *
     * Format: {hardScore}hard/{softScore}soft
     * - Hard score: Constraint violations (must be 0 for feasible solution)
     * - Soft score: Optimization quality (higher is better)
     *
     * Examples:
     * - "0hard/-50soft" = Feasible, soft score -50
     * - "-3hard/-100soft" = Infeasible, 3 hard constraint violations
     *
     * Calculated by TripCrewConstraintProvider.
     */
    @PlanningScore
    private HardSoftScore score;

    // ==================== HELPER METHODS ====================

    /**
     * Check if solution is feasible (no hard constraint violations).
     */
    public boolean isFeasible() {
        return score != null && score.hardScore() >= 0;
    }

    /**
     * Get total number of assignments.
     */
    public int getAssignmentCount() {
        return assignments != null ? assignments.size() : 0;
    }

    /**
     * Get number of available employees.
     */
    public int getEmployeeCount() {
        return employeeList != null ? employeeList.size() : 0;
    }

    /**
     * Get number of unfilled assignments.
     */
    public long getUnfilledCount() {
        if (assignments == null) return 0;
        return assignments.stream()
                .filter(a -> a.getAssignedEmployee() == null)
                .count();
    }

    /**
     * Get number of filled assignments.
     */
    public long getFilledCount() {
        if (assignments == null) return 0;
        return assignments.stream()
                .filter(a -> a.getAssignedEmployee() != null)
                .count();
    }

    /**
     * Check if all assignments have been filled.
     */
    public boolean isFullyAssigned() {
        return getUnfilledCount() == 0;
    }

    /**
     * Get hard score (constraint violations).
     */
    public int getHardScore() {
        return score != null ? score.hardScore() : Integer.MIN_VALUE;
    }

    /**
     * Get soft score (optimization quality).
     */
    public int getSoftScore() {
        return score != null ? score.softScore() : Integer.MIN_VALUE;
    }

    /**
     * Get human-readable score summary.
     */
    public String getScoreSummary() {
        if (score == null) {
            return "Not yet scored";
        }

        return String.format(
                "Score: %s | Feasible: %s | Filled: %d/%d",
                score,
                isFeasible(),
                getFilledCount(),
                getAssignmentCount()
        );
    }

    /**
     * Get list of unfilled assignments (for debugging).
     */
    public List<TripCrewAssignmentPlanning> getUnfilledAssignments() {
        if (assignments == null) return List.of();
        return assignments.stream()
                .filter(a -> a.getAssignedEmployee() == null)
                .toList();
    }

    /**
     * Get list of filled assignments.
     */
    public List<TripCrewAssignmentPlanning> getFilledAssignments() {
        if (assignments == null) return List.of();
        return assignments.stream()
                .filter(a -> a.getAssignedEmployee() != null)
                .toList();
    }

    @Override
    public String toString() {
        return String.format(
                "TripCrewScheduleSolution[assignments=%d, employees=%d, score=%s, feasible=%s]",
                getAssignmentCount(),
                getEmployeeCount(),
                score,
                isFeasible()
        );
    }
}
