package lk.ashan.routenetlkserverapllication.module.partreqest.repository;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `PartRequestStatus` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface PartRequestStatusRepository extends JpaRepository<PartRequestStatus, Integer> {

    /**
     * Finds a `PartRequestStatus` entity by its name.
     *
     * @param pending the name of the `PartRequestStatus` to find.
     * @return an `Optional` containing the `PartRequestStatus` if found, or empty if not found.
     */
    Optional<PartRequestStatus> findByName(String pending);
}
