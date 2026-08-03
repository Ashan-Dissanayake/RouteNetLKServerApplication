package lk.ashan.routenetlkserverapllication.module.partreqest.event;


import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;

public record PartRequestApprovedEvent(Branch branch, Integer partRequestId) {
}
