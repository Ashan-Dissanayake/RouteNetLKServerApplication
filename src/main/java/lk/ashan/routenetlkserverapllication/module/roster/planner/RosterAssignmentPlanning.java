package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Setter
@Getter
@PlanningEntity
@NoArgsConstructor  // Add Lombok annotation
@AllArgsConstructor
public class RosterAssignmentPlanning {
    @PlanningId
    private String id; // Format: "roster-{rosterId}-{role}"

    private Integer rosterId;
    private Integer requiredDesignationId; // 1 for Driver, 2 for Conductor
    private Integer branchId;
    private String shiftId;
    private java.time.LocalDate rosterDate;

    // Planning variable - the employee assigned to this slot
    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private EmployeePlanning employee;

    // Helper methods
    public boolean isDriverSlot() {
        return requiredDesignationId != null && requiredDesignationId == 1;
    }

    public boolean isConductorSlot() {
        return requiredDesignationId != null && requiredDesignationId == 2;
    }

    public boolean hasEmployee() {
        return employee != null;
    }
}
