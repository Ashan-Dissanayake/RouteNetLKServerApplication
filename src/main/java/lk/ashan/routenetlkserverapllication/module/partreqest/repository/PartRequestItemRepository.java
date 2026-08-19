package lk.ashan.routenetlkserverapllication.module.partreqest.repository;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `PartRequestItem` entities.
 * Extends the `JpaRepository` to provide CRUD operations and query methods.
 */
@Repository
public interface PartRequestItemRepository extends JpaRepository<PartRequestItem, Integer> {
}
