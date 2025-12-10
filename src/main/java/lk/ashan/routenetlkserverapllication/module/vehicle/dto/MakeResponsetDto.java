package lk.ashan.routenetlkserverapllication.module.vehicle.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MakeResponsetDto {
    private  Integer id;
    private  String name;
    private  boolean airconditioned;
}
