package lk.ashan.routenetlkserverapllication.module.incident.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `IncidentType` entities.
 * Extends the JpaRepository interface to provide CRUD operations and query methods.
 *
 * @author Ashan Dissanayake
 */
@Repository
public interface IncidentTypeRepository extends JpaRepository<IncidentType, Integer> {
}
