package lk.ashan.routenetlkserverapllication.module.vehicle.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleDetailResponseDto {
    private Integer id;
    private String number;
    private ModelDto model;
    private BusTypeDto bustype;
    private Integer mileage;
    private FueltypeDto fueltype;
    private ConditionrateDto conditionrate;
    private String remarks;
    private VehiclestatusDto vehiclestatus;
    private BranchSummaryDto branch;
}
