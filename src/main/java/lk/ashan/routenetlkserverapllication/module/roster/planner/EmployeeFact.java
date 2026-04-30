package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFact {
    private Integer id;
    private String fullname;
    private Integer designationId;
    private Integer familiarityLevel;

    public boolean isDriver() {
        return this.designationId != null && this.designationId == 1;
    }

    public boolean isConductor() {
        return this.designationId != null && this.designationId == 2;
    }

    public boolean hasRequiredFamiliarity(Integer requiredLevel) {
        if (this.familiarityLevel == null) return false;
        return this.familiarityLevel >= requiredLevel;
    }
}
