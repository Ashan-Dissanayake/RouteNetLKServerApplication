package lk.ashan.routenetlkserverapllication.module.grn.event;

import java.math.BigDecimal;

public record PartReceivedEvent(
        Integer partId,
        BigDecimal quantityReceived
) {}
