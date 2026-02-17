package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShiftRosterAssignmentDto {
    private Integer id;
    private ShiftSummaryResponseDto shift;
    private LocalDate doassigned;
    private RoleDto role;
    private EmployeeSummaryResponseDto employee;
}
