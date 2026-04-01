package lk.ashan.routenetlkserverapllication.module.grn.model.dto;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrnPartDto {
    private Integer id;
    private PartSummaryDto part;
    private BigDecimal quantity;
}
