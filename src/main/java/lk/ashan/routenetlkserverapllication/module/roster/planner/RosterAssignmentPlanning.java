package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningEntity
public class RosterAssignmentPlanning {

    @PlanningId
    private Integer id;

    // These are given, OptaPlanner does NOT change these

    private Roster roster;          // Which roster (week)
    private Shift shift;            // Which shift (Morning, Evening, Night)
    private Role role;              // Which role (Driver, Conductor)
    private LocalDate doassigned;   // Which date in the week


    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private EmployeeFact assignedEmployee;

}

