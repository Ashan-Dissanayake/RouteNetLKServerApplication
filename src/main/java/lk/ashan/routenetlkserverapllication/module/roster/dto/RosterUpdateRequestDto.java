package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class RosterUpdateRequestDto extends RosterRequestDto{
    @NotNull(message = "Roster ID is mandatory")
    private Integer id;
}
