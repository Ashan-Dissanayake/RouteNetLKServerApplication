package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleServicePartBulkCreateRequestDto {

    @NotEmpty(message = "At least one part must be provided")
    @Valid
    private List<VehicleServicePartCreateRequestDto> parts;

}
