package lk.ashan.routenetlkserverapllication.module.farecollection.event;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lombok.*;

public record FareReconciledEvent(Integer fareCollectionId, Branch branch) { }
