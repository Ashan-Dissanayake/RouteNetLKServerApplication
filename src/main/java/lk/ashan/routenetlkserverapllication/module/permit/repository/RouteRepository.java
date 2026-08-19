package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for managing `Route` entities.
 * Extends the JpaRepository interface to provide CRUD operations and query methods.
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
}
