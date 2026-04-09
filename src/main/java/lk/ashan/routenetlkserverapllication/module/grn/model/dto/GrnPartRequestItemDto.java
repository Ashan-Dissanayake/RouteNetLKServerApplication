package lk.ashan.routenetlkserverapllication.module.grn.model.dto;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestItemDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrnPartRequestItemDto {
    private Integer id;
    private BigDecimal quantity;
    private PartRequestItemDto partrequestitem;
}
