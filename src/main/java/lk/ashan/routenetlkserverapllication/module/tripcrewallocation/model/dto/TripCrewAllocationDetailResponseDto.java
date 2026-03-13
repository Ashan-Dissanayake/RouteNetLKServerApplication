package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.ShiftSummaryResponseDto;
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
