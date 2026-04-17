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

    public boolean isDriver() { return this.designationId == 1; }
    public boolean isConductor() { return this.designationId == 2; }
}
