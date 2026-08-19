package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `BranchType` entities.
 * Extends the JpaRepository interface to provide CRUD operations and query methods.
 */
@Repository
public interface BranchTypeRepository extends JpaRepository<BranchType, Integer> {
}
