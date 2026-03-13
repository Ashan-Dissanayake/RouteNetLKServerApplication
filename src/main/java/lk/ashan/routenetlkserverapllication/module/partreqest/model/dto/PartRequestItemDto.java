package lk.ashan.routenetlkserverapllication.module.partreqest.model.dto;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDetailResponseDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartRequestItemDto {
    private Integer id;
    private BigDecimal quantity;
    private PartSummaryDetailResponseDto part;
}
