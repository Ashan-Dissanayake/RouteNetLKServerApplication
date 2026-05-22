package lk.ashan.routenetlkserverapllication.module.incident.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentSummaryDto {
    private Integer id;
    private String name;
    private Integer regionalareaId;
}
