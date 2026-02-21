package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningSolution
public class RosterScheduleSolution {

    // ==================== PLANNING ENTITIES ====================
    // These are what OptaPlanner optimizes (assigns employees to)

    @PlanningEntityCollectionProperty
    private List<RosterAssignmentPlanning> assignments;

    // ==================== PROBLEM FACTS ====================
    // These are given inputs that OptaPlanner uses for constraints

    /**
     * Available employees that can be assigned
     * This is the value range for the assignedEmployee planning variable
     */
    @ValueRangeProvider(id = "employeeRange")
    @ProblemFactCollectionProperty
    private List<EmployeeFact> availableEmployees;

    /**
     * Existing fixed assignments (already confirmed/locked)
     * Used for conflict detection in constraints
     */
    @ProblemFactCollectionProperty
    private List<Shiftrosterassignment> existingAssignments;

    // ==================== PLANNING SCORE ====================
    // OptaPlanner calculates this based on RosterConstraintProvider

    @PlanningScore
    private HardSoftScore score;

    // ==================== HELPER METHODS ====================

    /**
     * Get total number of planning entities
     */
    public int getAssignmentCount() {
        return assignments != null ? assignments.size() : 0;
    }

    /**
     * Get total number of available employees
     */
    public int getEmployeeCount() {
        return availableEmployees != null ? availableEmployees.size() : 0;
    }

    /**
     * Check if solution is feasible (no hard constraint violations)
     */
    public boolean isFeasible() {
        return score != null && score.hardScore() >= 0;
    }
}
