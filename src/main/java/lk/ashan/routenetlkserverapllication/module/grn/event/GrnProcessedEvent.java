package lk.ashan.routenetlkserverapllication.module.grn.event;

public record GrnProcessedEvent(
        Integer partRequestId,
        Integer grnId,
        String statusName
) {}
