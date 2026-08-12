package lk.ashan.routenetlkserverapllication.module.incident.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidentCreateRequestDto{
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;
    @NotNull(message = "Trip Execution is mandatory")
    private TripExecutionSummaryDto tripexecution;
    @NotNull(message = "Incident type is mandatory")
    private IncidentTypeDto incidenttype;
    @NotNull(message = "Regional area is mandatory")
    private RegionalOffice regionalarea;
    @NotNull(message = "Time is mandatory")
    private LocalTime toreported;
    @NotNull(message = "Date is mandatory")
    private LocalDate doreported;
    @NotNull(message = "Description is mandatory")
    private String remarks;
    @NotNull(message = "Status is mandatory")
    private IncidentStatusDto incidentstatus;
}
