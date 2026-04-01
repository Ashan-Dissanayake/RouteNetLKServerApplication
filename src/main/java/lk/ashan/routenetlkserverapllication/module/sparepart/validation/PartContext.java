package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class PartContext {
    private Integer partId;
    private BigDecimal qoh;
    private BigDecimal rop;
    private BigDecimal maxlevel;
    private BigDecimal existingQoh;

}
