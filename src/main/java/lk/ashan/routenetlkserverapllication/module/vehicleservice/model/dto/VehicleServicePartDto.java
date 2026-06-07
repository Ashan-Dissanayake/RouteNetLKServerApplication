package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleServicePartDto {
    private Integer id;
    private VehicleServiceSummaryDto vehicleservice;
    private PartSummaryDto part;
    private BigDecimal quantity;
}
