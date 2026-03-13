package lk.ashan.routenetlkserverapllication.module.vehicle.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleSummaryResponseDto {
    private  Integer id;
    private String number;
}
