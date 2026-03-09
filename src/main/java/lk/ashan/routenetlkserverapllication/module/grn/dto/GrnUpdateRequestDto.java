package lk.ashan.routenetlkserverapllication.module.grn.dto;

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
