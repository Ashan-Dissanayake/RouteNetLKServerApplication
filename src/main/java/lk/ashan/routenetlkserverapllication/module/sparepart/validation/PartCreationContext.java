package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PartCreationContext {

    private BigDecimal qoh;
    private BigDecimal rop;

}
