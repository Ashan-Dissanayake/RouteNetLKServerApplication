package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lombok.Data;


@Data
public class RosterShiftAssignmentSummaryDto {
    private Integer id;
    private RosterShiftSummaryDto rostershift;
    private EmployeeSummaryDto employee;
}
