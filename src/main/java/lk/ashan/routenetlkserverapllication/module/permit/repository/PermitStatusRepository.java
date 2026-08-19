package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for managing `PermiteStatus` entities.
 * Extends the JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface PermitStatusRepository extends JpaRepository<PermiteStatus, Integer> {

    /**
     * Finds a `PermiteStatus` entity by its name.
     *
     * @param active the name of the `PermiteStatus` to find.
     * @return an `Optional` containing the found `PermiteStatus`, or empty if not found.
     */
    Optional<PermiteStatus> findByName(String active);
}
