package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository interface for managing `RosterShiftAssignment` entities.
 * Provides methods for querying and updating roster shift assignments.
 */
@Repository
public interface RosterShiftAssignmentRepository extends JpaRepository<RosterShiftAssignment, Integer> {

    /**
     * Finds unassigned roster shift assignments by the given roster ID.
     *
     * @param rosterId the ID of the roster to search for unassigned assignments
     * @return a list of unassigned `RosterShiftAssignment` entities
     */
    @Query("SELECT ra FROM RosterShiftAssignment ra " +
            "JOIN FETCH ra.rostershift rs " +
            "JOIN FETCH rs.shift s " +
            "WHERE rs.roster.id = :rosterId " +
            "AND ra.employee IS NULL")
    List<RosterShiftAssignment> findUnassignedByRosterId(@Param("rosterId") Integer rosterId);

    /**
     * Finds all roster shift assignments by the given roster ID, ordered by shift date and start time.
     *
     * @param rosterId the ID of the roster to search for assignments
     * @return a list of `RosterShiftAssignment` entities ordered by shift date and start time
     */
    @Query("SELECT ra FROM RosterShiftAssignment ra " +
            "JOIN ra.rostershift rs " +
            "WHERE rs.roster.id = :rosterId " +
            "ORDER BY rs.doshift ASC, rs.shift.tostart ASC")
    List<RosterShiftAssignment> findByRosterId(@Param("rosterId") Integer rosterId);

    /**
     * Updates the employee and status of a roster shift assignment directly by its ID.
     *
     * @param id the ID of the roster shift assignment to update
     * @param employeeId the ID of the employee to assign
     * @param statusId the ID of the status to set
     * @throws IllegalArgumentException if the update fails
     */
    @Modifying
    @Transactional
    @Query("UPDATE RosterShiftAssignment ra SET ra.employee.id = :employeeId, ra.rostershiftassignmentstatus.id = :statusId WHERE ra.id = :id")
    void updateEmployeeAndStatusDirectly(@Param("id") Integer id, @Param("employeeId") Integer employeeId, @Param("statusId") Integer statusId);

    /**
     * Counts the number of assignments for a specific roster shift.
     *
     * @param rosterShiftId the ID of the roster shift to count assignments for
     * @return the number of assignments for the given roster shift
     */
    @Query("SELECT COUNT(rsa) FROM RosterShiftAssignment rsa WHERE rsa.rostershift.id = :rosterShiftId")
    int countAssignmentsByRosterShiftId(@Param("rosterShiftId") Integer rosterShiftId);
}
