package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `Model` entities.
 * Extends the JpaRepository interface to provide CRUD operations and more.
 */
@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
}
