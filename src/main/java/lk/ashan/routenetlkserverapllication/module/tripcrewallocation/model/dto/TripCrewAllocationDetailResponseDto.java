package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCrewAllocationDetailResponseDto {
    private Integer id;
    private TripSummaryResponseDto trip;
    private RosterShiftAssignmentSummaryDto rostershiftassignment;
    private TripCrewAllocationStatusDto tripallocationstatus;
    private String remarks;
    private LocalTime allocatedAt;
}
