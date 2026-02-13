package lk.ashan.routenetlkserverapllication.module.trip.dto;

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
public class TripUpdateRequestDto extends TripRequestDto{

    private Integer id;
}
