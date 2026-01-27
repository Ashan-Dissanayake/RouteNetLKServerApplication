package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class RosterCreateRequestDto extends RosterRequestDto{
private Integer id;
}
