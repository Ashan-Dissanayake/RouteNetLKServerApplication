package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.RouteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `RouteType` entities.
 * Extends the `JpaRepository` to provide CRUD operations and query methods.
 */
@Repository
public interface RouteTypeRepository extends JpaRepository<RouteType, Integer> {
}
