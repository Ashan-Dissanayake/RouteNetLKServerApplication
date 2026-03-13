package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestSummaryResponseDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleServicePartDetailResponseDto {
    private Integer id;
    private VehicleServiceSummaryResponseDto vehicleservice;
    private PartRequestSummaryResponseDto part;
    private BigDecimal quantity;


}
