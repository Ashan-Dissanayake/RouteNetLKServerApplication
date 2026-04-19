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

@Repository
public interface RosterShiftAssignmentRepository extends JpaRepository<RosterShiftAssignment, Integer> {
    @Query("SELECT ra FROM RosterShiftAssignment ra " +
            "JOIN FETCH ra.rostershift rs " +
            "JOIN FETCH rs.shift s " +
            "WHERE rs.roster.id = :rosterId " +
            "AND ra.employee IS NULL")
    List<RosterShiftAssignment> findUnassignedByRosterId(@Param("rosterId") Integer rosterId);

    @Query("SELECT ra FROM RosterShiftAssignment ra " +
            "JOIN ra.rostershift rs " +
            "WHERE rs.roster.id = :rosterId " +
            "ORDER BY rs.doshift ASC, rs.shift.tostart ASC")
    List<RosterShiftAssignment> findByRosterId(@Param("rosterId") Integer rosterId);

    @Modifying
    @Transactional
    @Query("UPDATE RosterShiftAssignment ra SET ra.employee.id = :employeeId, ra.rostershiftassignmentstatus.id = :statusId WHERE ra.id = :id")
    void updateEmployeeAndStatusDirectly(@Param("id") Integer id, @Param("employeeId") Integer employeeId, @Param("statusId") Integer statusId);


}
