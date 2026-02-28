package lk.ashan.routenetlkserverapllication.module.incident.dto;

import lk.ashan.routenetlkserverapllication.module.trip.dto.TripSummaryResponseDto;
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
