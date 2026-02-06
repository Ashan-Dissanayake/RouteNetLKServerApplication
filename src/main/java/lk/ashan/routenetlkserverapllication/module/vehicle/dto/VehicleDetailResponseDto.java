package lk.ashan.routenetlkserverapllication.module.vehicle.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.time.Year;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleDetailResponseDto {
    private Integer id;
    private String number;
    private Integer mileage;
    private String remarks;
    private FueltypeDto fueltype;
    private ConditionrateDto conditionrate;
    private VehiclestatusDto vehiclestatus;
    private BranchSummaryResponseDto branch;
    private ModelDto model;
}
