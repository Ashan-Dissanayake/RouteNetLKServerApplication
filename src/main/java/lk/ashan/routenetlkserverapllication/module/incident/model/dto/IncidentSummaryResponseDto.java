package lk.ashan.routenetlkserverapllication.module.incident.model.dto;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentSummaryResponseDto {
    private Integer id;
    private TripSummaryResponseDto trip;

}
