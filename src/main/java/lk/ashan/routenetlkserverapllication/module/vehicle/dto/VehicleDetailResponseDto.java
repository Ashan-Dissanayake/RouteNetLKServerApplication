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
    private String code;
    private String number;
    private Year yom;
    private LocalDate dob;
    private Integer mileage;
    private String chasisnumber;
    private String enginenumber;
    private String remarks;
    private FueltypeDto fueltype;
    private ConditionrateDto conditionrate;
    private ServicetypeDto servicetype;
    private VehiclestatusDto vehiclestatus;
    private SeatingcapacityResponseDto seatingcapacity;
    private EmployeeSummaryResponseDto employee;
    private BranchSummaryResponseDto branch;
}
