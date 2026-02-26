package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFact {

    private Integer id;
    private String number;          // Employee number (e.g., EMPCLM0001)
    private String fullname;
    private Integer branchId;       // Must match shift branch


    private List<Integer> qualifiedRoles;

    private Integer licenseCategoryId;
    private Integer routeFamiliarityLevelId;

    private List<Integer> preferredShiftIds;


    private Integer hourlyRate;
    private Integer experienceYears;

    private String status;
    private List<Integer> unavailableDaysOfWeek;
    private Integer preferredMaxHoursPerWeek;

    // ==================== HELPER METHODS ====================

    public boolean isQualifiedForRole(Integer roleId) {
        return qualifiedRoles != null && qualifiedRoles.contains(roleId);
    }

    public boolean isDriver() {
        return qualifiedRoles != null && qualifiedRoles.contains(1);
    }

    public boolean isConductor() {
        return qualifiedRoles != null && qualifiedRoles.contains(2);
    }

    public boolean isDualQualified() {
        return isDriver() && isConductor();
    }

    public boolean hasLicenseCategory() {
        return licenseCategoryId != null;
    }

    public boolean hasRouteFamiliarity() {
        return routeFamiliarityLevelId != null;
    }

    @Override
    public String toString() {
        return String.format("Employee[id=%d, number=%s, name=%s, roles=%s, branch=%d]",
                id, number, fullname, qualifiedRoles, branchId);
    }
}
