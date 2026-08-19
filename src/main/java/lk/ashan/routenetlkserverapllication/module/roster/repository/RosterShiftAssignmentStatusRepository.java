package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing RosterShiftAssignmentStatus entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface RosterShiftAssignmentStatusRepository extends JpaRepository<RosterShiftAssignmentStatus, Integer> {

    /**
     * Finds a RosterShiftAssignmentStatus entity by its name.
     *
     * @param name the name of the RosterShiftAssignmentStatus to find
     * @return an Optional containing the found RosterShiftAssignmentStatus, or empty if not found
     */
    Optional<RosterShiftAssignmentStatus> findByName(String name);
}
