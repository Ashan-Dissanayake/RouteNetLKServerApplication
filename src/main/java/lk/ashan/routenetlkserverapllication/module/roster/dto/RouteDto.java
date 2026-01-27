package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RouteDto {
   private Integer id;
   private String number;
   private String name;
}
