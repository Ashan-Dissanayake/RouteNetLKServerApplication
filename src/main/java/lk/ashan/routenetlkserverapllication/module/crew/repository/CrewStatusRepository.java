package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing CrewStatus entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface CrewStatusRepository extends JpaRepository<CrewStatus, Integer> {

    /**
     * Finds a CrewStatus entity by its name.
     *
     * @param assigned the name of the CrewStatus to find.
     * @return an Optional containing the CrewStatus if found, or empty if not found.
     */
    Optional<CrewStatus> findByName(String assigned);
}
