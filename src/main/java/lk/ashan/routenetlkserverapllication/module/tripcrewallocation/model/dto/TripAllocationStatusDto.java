package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripAllocationStatusDto{
    private  Integer id;
    private String name;
}
