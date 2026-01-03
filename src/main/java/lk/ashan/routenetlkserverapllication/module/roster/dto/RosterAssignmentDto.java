package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RosterAssignmentDto {
    private Integer id;
    private EmployeeSummaryResponseDto employee;
}
