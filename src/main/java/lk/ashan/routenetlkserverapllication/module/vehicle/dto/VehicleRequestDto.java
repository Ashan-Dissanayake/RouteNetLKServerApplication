package lk.ashan.routenetlkserverapllication.module.vehicle.dto;

import jakarta.validation.constraints.*;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.pattern.ValidBus;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.modelchassis.ValidModelChassis;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.modelengine.ValidModelEngine;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Year;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@ValidBus
@ValidModelChassis
//@ValidModelEngine
public class VehicleRequestDto {

    @NotBlank(message = "Code Can Not be Empty")
    @Pattern(regexp = "^BS-[A-Z]{3}[0-9]{5}$",message = "Invalid Code")
    private String code;

    @NotBlank(message = "Plate Number Can Not be Empty")
    @Pattern(regexp = "^N[A-Z]-[0-9]{4}$",message = "Invalid Plate Number")
    private String number;

    @NotNull(message = "YOM Can Not be Empty")
    @PastOrPresent(message = "YOM Date Can Not be in the Future")
    private Year yom;

    @NotNull(message = "DOB Can Not be Empty")
    @PastOrPresent(message = "DOB Date Can Not be in the Future")
    private LocalDate dob;

    @NotNull(message = "Mileage Can Not be Empty")
    @Digits(integer = 7, fraction = 2,message = "Numeric value out of bounds (<7 digits> expected)")
    @Positive(message = "Mileage must be positive")
    private Integer mileage;

    @NotBlank(message = "Chassis Number Can Not be Empty")
    private String chasisnumber;

    @NotBlank(message = "Engine Number Can Not be Empty")
    private String enginenumber;

    private String remarks;

    @NotNull(message = "Make/Mode Can Not be Empty")
    private MakeDto make;

    @NotNull(message = "Fuel Type Can Not be Empty")
    private FueltypeDto fueltype;

    @NotNull(message = "Condition Rate Can Not be Empty")
    private ConditionrateDto conditionrate;

    @NotNull(message = "Service Type Can Not be Empty")
    private ServicetypeDto servicetype;

    @NotNull(message = "Vehicle Status Can Not be Empty")
    private VehiclestatusDto vehiclestatus;

    @NotNull(message = "Seating Capacity Can Not be Empty")
    private SeatingcapacityDto seatingcapacity;

    @NotNull(message = "Employee Can Not be Empty")
    private EmployeeSummaryResponseDto employee;

    @NotNull(message = "Branch Not be Empty")
    private BranchSummaryResponseDto branch;

}
