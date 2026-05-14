package lk.ashan.routenetlkserverapllication.module.permit.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RouteSummaryResponseDto {
    private Integer id;
    private String name;
    private List<WaypointDto> waypoints;
}

