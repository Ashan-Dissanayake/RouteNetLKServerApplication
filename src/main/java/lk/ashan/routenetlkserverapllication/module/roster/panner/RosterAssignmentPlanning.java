package lk.ashan.routenetlkserverapllication.module.roster.panner;

import lk.ashan.routenetlkserverapllication.module.roster.model.Role;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shift;
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

    public long getShiftDurationHours() {
        if (shift == null) return 0;

        var start = shift.getTostart();
        var end = shift.getToend();

        // Handle overnight shifts
        if (end.isBefore(start)) {
            return java.time.Duration.between(start, java.time.LocalTime.MAX).toHours() +
                    java.time.Duration.between(java.time.LocalTime.MIN, end).toHours() + 1;
        }

        return java.time.Duration.between(start, end).toHours();
    }

    public boolean isWeekend() {
        if (doassigned == null) return false;
        var dayOfWeek = doassigned.getDayOfWeek();
        return dayOfWeek == java.time.DayOfWeek.SATURDAY ||
                dayOfWeek == java.time.DayOfWeek.SUNDAY;
    }

    public boolean isNightShift() {
        if (shift == null) return false;
        var start = shift.getTostart();
        return start.isAfter(java.time.LocalTime.of(20, 0)) ||
                start.isBefore(java.time.LocalTime.of(6, 0));
    }
}

