package lk.ashan.routenetlkserverapllication.module.trip.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DirectionDto{
    private Integer id;
    private String name;
}
