package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing BranchStatus entities.
 * Provides methods for performing CRUD operations and custom queries.
 */
@Repository
public interface BranchStatusRepository extends JpaRepository<BranchStatus, Integer> {

    /**
     * Finds a BranchStatus entity by its name.
     *
     * @param name the name of the BranchStatus to find
     * @return an Optional containing the found BranchStatus, or empty if not found
     */
    Optional<BranchStatus> findByName(String name);
}
