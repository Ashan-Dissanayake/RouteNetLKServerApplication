package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripStatusDto{
    private Integer id;
    private String name;
}
