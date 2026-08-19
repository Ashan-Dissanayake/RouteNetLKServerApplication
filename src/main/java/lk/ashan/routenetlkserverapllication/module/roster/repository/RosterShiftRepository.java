package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing RosterShift entities.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
@Repository
public interface RosterShiftRepository extends JpaRepository<RosterShift, Integer> {

    /**
     * Finds all RosterShift entities associated with a specific roster ID.
     *
     * @param rosterId the ID of the roster to find shifts for
     * @return an Optional containing a list of RosterShift entities, or an empty Optional if none are found
     */
    Optional<List<RosterShift>> findByRoster_Id(Integer rosterId);

    /**
     * Retrieves all RosterShift entities for the current date associated with a specific branch ID.
     *
     * @param branchId the ID of the branch to find today's shifts for
     * @return a list of RosterShift entities for the specified branch and current date
     */
    @Query(value = "SELECT rs.* FROM rostershift rs " +
            "INNER JOIN roster r ON rs.roster_id = r.id " +
            "WHERE r.branch_id = :branchId " +
            "AND rs.doshift = CURRENT_DATE",
            nativeQuery = true)
    List<RosterShift> findTodayShiftsByBranchNative(@Param("branchId") Integer branchId);

}
