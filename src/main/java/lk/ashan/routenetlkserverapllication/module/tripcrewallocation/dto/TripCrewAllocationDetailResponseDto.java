package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.dto;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftSummaryResponseDto;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCrewAllocationDetailResponseDto {
    private Integer id;
    private Integer tripId;
    private EmployeeSummaryResponseDto employee;
    private RoleDto role;
    private ShiftSummaryResponseDto derivedShift;
    private TripAllocationStatusDto allocationstatus;
    private String remarks;
    private LocalTime allocatedAt;
}
