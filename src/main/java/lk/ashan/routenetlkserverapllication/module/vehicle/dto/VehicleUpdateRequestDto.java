package lk.ashan.routenetlkserverapllication.module.vehicle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class VehicleUpdateRequestDto extends VehicleRequestDto {
    @NotNull
    private Integer id;
}
