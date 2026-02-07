package lk.ashan.routenetlkserverapllication.module.permit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RouteDto {
    private Integer id;
    private String number;
    private Integer distancekm;
    private ScheduleTypeDto scheduletype;
    private RouteTypeDto routetype;


}
