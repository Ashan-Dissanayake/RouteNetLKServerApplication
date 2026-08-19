package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `RouteFamiliarityLevel` entities.
 * Extends the `JpaRepository` to provide CRUD operations and query methods.
 *
 * @see JpaRepository
 */
@Repository
public interface RouteFamiliarityLevelRepository extends JpaRepository<RouteFamiliarityLevel, Integer> {
}
