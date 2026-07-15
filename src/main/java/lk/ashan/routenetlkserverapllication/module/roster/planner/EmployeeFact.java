package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an employee fact with details such as ID, full name, designation, and familiarity level.
 * Provides utility methods to determine the employee's role and familiarity level.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFact {
    private Integer id;
    private String fullname;
    private Integer designationId;
    private Integer familiarityLevel;

    /**
     * Checks if the employee is a driver.
     *
     * @return {@code true} if the employee's designation ID is 1, otherwise {@code false}.
     */
    public boolean isDriver() {
        return this.designationId != null && this.designationId == 1;
    }

    /**
     * Checks if the employee is a conductor.
     *
     * @return {@code true} if the employee's designation ID is 2, otherwise {@code false}.
     */
    public boolean isConductor() {
        return this.designationId != null && this.designationId == 2;
    }

    /**
     * Checks if the employee has the required familiarity level.
     *
     * @param requiredLevel the required familiarity level to check against.
     * @return {@code true} if the employee's familiarity level is greater than or equal to the required level,
     *         otherwise {@code false}.
     */
    public boolean hasRequiredFamiliarity(Integer requiredLevel) {
        if (this.familiarityLevel == null) return false;
        return this.familiarityLevel >= requiredLevel;
    }
}
