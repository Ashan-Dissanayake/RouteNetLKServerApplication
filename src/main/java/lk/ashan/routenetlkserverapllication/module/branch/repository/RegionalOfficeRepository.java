package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `RegionalOffice` entities.
 * Extends the `JpaRepository` to provide CRUD operations and query methods.
 *
 * @author Ashan
 */
@Repository
public interface RegionalOfficeRepository extends JpaRepository<RegionalOffice, Integer> {
}
