package lk.ashan.routenetlkserverapllication.module.vehicle.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SeatingcapacityRequestDto {
    private Integer id;
    private Integer amount;
}
