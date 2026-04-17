package lk.ashan.routenetlkserverapllication.module.roster.planner;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@PlanningSolution
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RosterShiftAssignmentSolution {

    @ValueRangeProvider(id = "employeeRange")
    @ProblemFactCollectionProperty
    private List<EmployeeFact> employeeList;

    @PlanningEntityCollectionProperty
    private List<RosterShiftAssignmentPlanning> assignmentList;

    @PlanningScore
    private HardSoftScore score;

    public RosterShiftAssignmentSolution(List<EmployeeFact> employeeList, List<RosterShiftAssignmentPlanning> assignmentList) {
        this.employeeList = employeeList;
        this.assignmentList = assignmentList;
    }
}
