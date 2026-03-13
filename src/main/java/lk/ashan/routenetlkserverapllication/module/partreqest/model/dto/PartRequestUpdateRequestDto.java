package lk.ashan.routenetlkserverapllication.module.partreqest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PartRequestUpdateRequestDto extends PartRequestDto{

    private Integer id;
}
