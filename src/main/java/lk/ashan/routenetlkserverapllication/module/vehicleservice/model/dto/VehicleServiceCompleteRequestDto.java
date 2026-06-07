package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleServiceCompleteRequestDto {

    @NotBlank(message = "Completion remarks/technical report is required")
    private String remarks;

    @NotNull(message = "Service interval distance (in KM) is required")
    @Min(value = 1, message = "Service interval must be at least 1 KM")
    private Integer serviceIntervalKm;
}
