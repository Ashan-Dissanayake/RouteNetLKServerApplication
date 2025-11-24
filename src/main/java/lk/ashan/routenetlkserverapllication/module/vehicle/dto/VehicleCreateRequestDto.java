package lk.ashan.routenetlkserverapllication.module.vehicle.dto;

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
public class VehicleCreateRequestDto extends VehicleRequestDto{
}
