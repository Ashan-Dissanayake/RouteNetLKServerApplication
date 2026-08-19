package lk.ashan.routenetlkserverapllication.module.grn.event;

/**
 * Represents an event that is triggered when a GRN (Goods Received Note) is processed.
 *
 * @param partRequestId the ID of the part request associated with the GRN
 * @param grnId the ID of the processed GRN
 * @param statusName the status name of the processed GRN
 */
public record GrnProcessedEvent(
        Integer partRequestId,
        Integer grnId,
        String statusName
) {}
