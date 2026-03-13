package lk.ashan.routenetlkserverapllication.module.incident.model.dto;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
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
    private TripSummaryResponseDto trip;
    private IncidentStatusDto incidentstatus;
    private IncidentTypeDto incidenttype;
    private String remarks;
    private LocalTime toreported;
    private LocalDate doreported;
}
