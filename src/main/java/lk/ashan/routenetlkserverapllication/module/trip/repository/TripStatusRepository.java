package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Tripstatus entities.
 * Provides methods for performing CRUD operations and custom queries.
 */
@Repository
public interface TripStatusRepository extends JpaRepository<Tripstatus, Integer> {

    /**
     * Finds a Tripstatus entity by its name.
     *
     * @param name the name of the Tripstatus entity to find
     * @return an Optional containing the Tripstatus entity if found, or empty if not found
     */
    Optional<Tripstatus> findByName(String name);
}
