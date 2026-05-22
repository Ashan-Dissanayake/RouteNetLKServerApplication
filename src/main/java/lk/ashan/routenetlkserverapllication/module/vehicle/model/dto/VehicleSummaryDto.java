package lk.ashan.routenetlkserverapllication.module.vehicle.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleSummaryDto {
    private  Integer id;
    private String number;
    private Integer branchId;
}
