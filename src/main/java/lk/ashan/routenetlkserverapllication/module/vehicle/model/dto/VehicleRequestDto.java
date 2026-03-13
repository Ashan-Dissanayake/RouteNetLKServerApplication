package lk.ashan.routenetlkserverapllication.module.vehicle.model.dto;

import jakarta.validation.constraints.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class VehicleRequestDto {

    @NotBlank(message = "Plate Number Can Not be Empty")
    @Pattern(regexp = "^N[A-Z]-[0-9]{4}$",message = "Invalid Plate Number")
    private String number;

    @NotNull(message = "Mileage Can Not be Empty")
    @Digits(integer = 7, fraction = 2,message = "Numeric value out of bounds (<7 digits> expected)")
    @Positive(message = "Mileage must be positive")
    private Integer mileage;

    private String remarks;

    @NotNull(message = "Make/Mode Can Not be Empty")
    private MakeRequestDto make;

    @NotNull(message = "Fuel Type Can Not be Empty")
    private FueltypeDto fueltype;

    @NotNull(message = "Condition Rate Can Not be Empty")
    private ConditionrateDto conditionrate;

    @NotNull(message = "Vehicle Status Can Not be Empty")
    private VehiclestatusDto vehiclestatus;

    @NotNull(message = "Branch Can Not be Empty")
    private BranchSummaryResponseDto branch;

    @NotNull(message = "Model can not be Empty")
    private ModelDto model;

}
