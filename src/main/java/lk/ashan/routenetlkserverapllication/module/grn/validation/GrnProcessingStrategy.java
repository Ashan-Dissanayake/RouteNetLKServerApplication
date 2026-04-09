package lk.ashan.routenetlkserverapllication.module.grn.validation;

import java.math.BigDecimal;

public interface GrnProcessingStrategy {
    boolean isApplicable(BigDecimal receivedQty, BigDecimal expectedQty);
    void process(GrnContext context);
}
