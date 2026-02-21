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

    // ==================== PREFERENCE DATA ====================


    private List<Integer> preferredShiftIds;


    private Integer hourlyRate;
    private Integer experienceYears;

    private String status;
    private List<Integer> unavailableDaysOfWeek;
    private Integer preferredMaxHoursPerWeek;
}
