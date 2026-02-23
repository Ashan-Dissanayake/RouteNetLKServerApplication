package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.dto;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripSummaryResponseDto;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripCrewAllocationDetailResponseDto {
    private  Integer id;
    private TripSummaryResponseDto trip;
    private EmployeeSummaryResponseDto employee;
    private RoleDto role;
    private ShiftSummaryResponseDto derivedshift;
    private TripAllocationStatusDto tripallocationstatus;
}
