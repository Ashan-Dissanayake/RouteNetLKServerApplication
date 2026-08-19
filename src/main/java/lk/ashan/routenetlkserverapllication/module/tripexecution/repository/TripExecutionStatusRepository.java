package lk.ashan.routenetlkserverapllication.module.tripexecution.repository;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for managing `TripExecutionStatus` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface TripExecutionStatusRepository extends JpaRepository<TripExecutionStatus, Integer> {

    /**
     * Finds a `TripExecutionStatus` entity by its name.
     *
     * @param scheduled the name of the `TripExecutionStatus` to find.
     * @return an `Optional` containing the found `TripExecutionStatus`, or empty if not found.
     */
    Optional<TripExecutionStatus> findByName(String scheduled);
}
