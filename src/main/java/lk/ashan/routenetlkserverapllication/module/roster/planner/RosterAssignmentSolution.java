package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lombok.Getter;
import lombok.Setter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@PlanningSolution
public class RosterAssignmentSolution {

    @ProblemFactCollectionProperty
    private List<EmployeePlanning> allEmployees = new ArrayList<>();

    @PlanningEntityCollectionProperty
    private List<RosterAssignmentPlanning> assignmentList = new ArrayList<>();

    @PlanningScore
    private HardSoftScore score;

    /**
     * Provides the range of all eligible employees for assignment.
     * OptaPlanner will choose from this pool based on constraints.
     */
    @ValueRangeProvider(id = "employeeRange")
    public List<EmployeePlanning> getEmployeeRange() {
        if (allEmployees == null) {
            return new ArrayList<>();
        }

        return allEmployees.stream()
                .filter(EmployeePlanning::isEligible)
                .collect(Collectors.toList());
    }

    /**
     * Get all driver assignments
     */
    public List<RosterAssignmentPlanning> getDriverAssignments() {
        return assignmentList.stream()
                .filter(RosterAssignmentPlanning::isDriverSlot)
                .collect(Collectors.toList());
    }

    /**
     * Get all conductor assignments
     */
    public List<RosterAssignmentPlanning> getConductorAssignments() {
        return assignmentList.stream()
                .filter(RosterAssignmentPlanning::isConductorSlot)
                .collect(Collectors.toList());
    }

    /**
     * Get all unassigned slots
     */
    public List<RosterAssignmentPlanning> getUnassignedSlots() {
        return assignmentList.stream()
                .filter(a -> a.getEmployee() == null)
                .collect(Collectors.toList());
    }

    /**
     * Check if solution is fully assigned
     */
    public boolean isFullyAssigned() {
        return assignmentList.stream().allMatch(RosterAssignmentPlanning::hasEmployee);
    }

    /**
     * Get assignment count
     */
    public int getAssignmentCount() {
        return (int) assignmentList.stream()
                .filter(RosterAssignmentPlanning::hasEmployee)
                .count();
    }

    /**
     * Get total slots
     */
    public int getTotalSlots() {
        return assignmentList.size();
    }
}
