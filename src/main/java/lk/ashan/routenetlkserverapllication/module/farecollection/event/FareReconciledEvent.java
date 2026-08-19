package lk.ashan.routenetlkserverapllication.module.farecollection.event;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lombok.*;

/**
 * Event representing the reconciliation of a fare collection.
 *
 * @param fareCollectionId the unique identifier of the fare collection
 * @param branch the branch associated with the fare collection
 */
public record FareReconciledEvent(Integer fareCollectionId, Branch branch) { }
