package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.dto;

import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class VehicleServicePartRequestDto {
    @NotNull(message = "Service is required")
    private VehicleServiceSummaryResponseDto vehicleservice;

    @NotNull(message = "Service is required")
    private PartRequestSummaryResponseDto part;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.001", message = "Quantity must be greater than 0")
    private BigDecimal quantity;

}
