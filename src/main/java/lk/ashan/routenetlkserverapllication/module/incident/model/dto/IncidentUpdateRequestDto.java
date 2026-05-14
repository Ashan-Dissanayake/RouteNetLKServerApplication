package lk.ashan.routenetlkserverapllication.module.incident.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentUpdateRequestDto{
    @NotNull
    private Integer id;
    @NotNull(message = "Trip is mandatory")
    private TripSummaryResponseDto trip;
    @NotNull(message = "Incident type is mandatory")
    private IncidentTypeDto incidenttype;
    @NotNull(message = "Remark is mandatory")
    private String remarks;
    @NotNull(message = "Time is mandatory")
    private LocalTime toreported;
    @NotNull(message = "Date is mandatory")
    private LocalDate doreported;
    @NotNull(message = "Status is mandatory")
    private IncidentStatusDto incidentstatus;
}
