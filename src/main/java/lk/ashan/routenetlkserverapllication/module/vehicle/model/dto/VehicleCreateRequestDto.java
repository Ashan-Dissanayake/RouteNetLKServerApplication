package lk.ashan.routenetlkserverapllication.module.vehicle.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCreateRequestDto{
    @NotBlank(message = "Plate Number Can Not be Empty")
    @Pattern(regexp = "^N[A-Z]-[0-9]{4}$",message = "Invalid Plate Number")
    private String number;

    //    @NotNull(message = "Mileage Can Not be Empty")
    //@Digits(integer = 7, fraction = 2,message = "Numeric value out of bounds (<7 digits> expected)")
    @Positive(message = "Mileage must be positive")
    private Integer mileage;

    private String remarks;

    @NotNull(message = "Fuel Type Can Not be Empty")
    private FueltypeDto fueltype;

    @NotNull(message = "Condition Rate Can Not be Empty")
    private ConditionrateDto conditionrate;

    @NotNull(message = "Vehicle Status Can Not be Empty")
    private VehiclestatusDto vehiclestatus;

//    @NotNull(message = "Branch Can Not be Empty")
//    private BranchSummaryDto branch;

    @NotNull(message = "Model can not be Empty")
    private ModelDto model;

    @NotNull(message = "Bus type can not be Empty")
    private BusTypeDto bustype;
}
