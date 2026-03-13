package lk.ashan.routenetlkserverapllication.module.grn.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GrnUpdateRequestDto extends GrnRequestDto{

    private Integer id;
}
