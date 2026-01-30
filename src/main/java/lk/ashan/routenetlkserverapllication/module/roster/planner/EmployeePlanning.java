package lk.ashan.routenetlkserverapllication.module.roster.planner;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePlanning {

    private Integer id;
    private String number;
    private String name;
    private Integer branchId;
    private Integer designationId; // 1 = Driver, 2 = Conductor

    // Role identification
    private boolean isDriver;
    private boolean isConductor;

    // Driver-specific fields
    private Integer licenseCategoryId;
    private LocalDate licenseExpiry;
    private Set<Integer> allowedBusTypeIds = new HashSet<>();

    // Common fields
    private Integer crewStatusId; // 1 = Eligible, 3 = Active
    private LocalDate medicalExpiry;
    private Integer routeFamiliarityLevelId; // 1 = Low, 2 = Medium, 3 = High

    /**
     * Check if driver has a valid license on the given date
     */
    public boolean hasValidLicense(LocalDate date) {
        return isDriver && licenseExpiry != null && !licenseExpiry.isBefore(date);
    }

    /**
     * Check if employee has a valid medical certificate on the given date
     */
    public boolean hasValidMedical(LocalDate date) {
        return medicalExpiry != null && !medicalExpiry.isBefore(date);
    }

    /**
     * Check if employee is eligible for assignment
     */
    public boolean isEligible() {
        return crewStatusId != null && (crewStatusId == 1 || crewStatusId == 3);
    }

    /**
     * Check if employee matches the required designation
     */
    public boolean matchesDesignation(Integer requiredDesignationId) {
        return designationId != null && designationId.equals(requiredDesignationId);
    }

    /**
     * Check if employee belongs to the specified branch
     */
    public boolean belongsToBranch(Integer branchId) {
        return this.branchId != null && this.branchId.equals(branchId);
    }
}
