package lk.ashan.routenetlkserverapllication.module.grn.validation;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnPartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GrnContext {
    private Grn grn;
    private Integer partRequestId;
    private BigDecimal receivedQty;
    private BigDecimal expectedQty;
    private List<GrnPartRequestItemDto> itemUpdates;
}
