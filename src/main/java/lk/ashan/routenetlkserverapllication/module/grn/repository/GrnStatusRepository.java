package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `GrnStatus` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface GrnStatusRepository extends JpaRepository<GrnStatus, Integer> {

    /**
     * Finds a `GrnStatus` entity by its name.
     *
     * @param pending the name of the `GrnStatus` to find.
     * @return an `Optional` containing the `GrnStatus` if found, or empty if not found.
     */
    Optional<GrnStatus> findByName(String pending);
}
