package lk.ashan.routenetlkserverapllication.module.roster.planner;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@PlanningEntity
@Data
public class RosterShiftAssignmentPlanning {

    @PlanningId
    private Integer id;
    private Integer rosterShiftId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate shiftDate;
    private Integer designationId;
    private Integer shiftId;
    private Integer requiredFamiliarityLevel;

    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private EmployeeFact employeeFact;

}
