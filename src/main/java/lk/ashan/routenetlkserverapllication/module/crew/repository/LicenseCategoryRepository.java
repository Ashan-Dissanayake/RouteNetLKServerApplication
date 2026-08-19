package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.LicenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `LicenseCategory` entities.
 * Extends the `JpaRepository` to provide CRUD operations and query methods.
 */
@Repository
public interface LicenseCategoryRepository extends JpaRepository<LicenseCategory, Integer> {
}
