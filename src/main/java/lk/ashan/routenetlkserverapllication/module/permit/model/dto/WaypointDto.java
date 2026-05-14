package lk.ashan.routenetlkserverapllication.module.permit.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaypointDto {
    private Integer order;
    private String location;
    private Double lat;
    private Double lng;
}
