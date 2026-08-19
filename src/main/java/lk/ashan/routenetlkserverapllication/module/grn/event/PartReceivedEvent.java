package lk.ashan.routenetlkserverapllication.module.grn.event;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;

import java.math.BigDecimal;

/**
 * Event representing the receipt of a part at a specific branch.
 *
 * @param branch           The branch where the part was received.
 * @param partId           The unique identifier of the received part.
 * @param quantityReceived The quantity of the part that was received.
 */
public record PartReceivedEvent(
        Branch branch,
        Integer partId,
        BigDecimal quantityReceived
) {}
