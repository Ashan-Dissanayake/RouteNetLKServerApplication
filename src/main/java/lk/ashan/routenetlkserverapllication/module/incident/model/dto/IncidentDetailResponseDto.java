package lk.ashan.routenetlkserverapllication.module.incident.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalOfficeDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentDetailResponseDto {
    private Integer id;
    private BranchSummaryDto branch;
    private TripExecutionSummaryDto tripexecution;
    private IncidentTypeDto incidenttype;
    private RegionalOfficeDto regionalarea;
    private LocalTime toreported;
    private LocalDate doreported;
    private Integer odometeratincident;
    private String remarks;
    private IncidentStatusDto incidentstatus;

}
