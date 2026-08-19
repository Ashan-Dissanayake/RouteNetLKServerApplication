package lk.ashan.routenetlkserverapllication.module.incident.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `IncidentStatus` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface IncidentStatusRepository extends JpaRepository<IncidentStatus, Integer> {

    /**
     * Finds an `IncidentStatus` entity by its name.
     *
     * @param inProgress the name of the incident status to search for.
     * @return an `Optional` containing the `IncidentStatus` if found, or empty if not found.
     */
    Optional<IncidentStatus> findByName(String inProgress);
}
