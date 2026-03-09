package lk.ashan.routenetlkserverapllication.module.grn.dto;

import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartSummaryDetailResponseDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrnPartDto {
    private Integer id;
    private PartSummaryDetailResponseDto part;
    private BigDecimal quantity;
}
