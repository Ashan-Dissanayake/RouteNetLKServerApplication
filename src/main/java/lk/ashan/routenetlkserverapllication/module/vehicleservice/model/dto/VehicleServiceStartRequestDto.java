package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleServiceStartRequestDto {

    @NotNull(message = "Start odometer reading is required")
    @Min(value = 0, message = "Start odometer reading cannot be negative")
    private Integer startodometer;

    @NotNull(message = "Main technician ID is required")
    private Integer maintechnicianId;
}
