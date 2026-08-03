package lk.ashan.routenetlkserverapllication.module.grn.event;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;

import java.math.BigDecimal;

public record PartReceivedEvent(
        Branch branch,
        Integer partId,
        BigDecimal quantityReceived
) {}
