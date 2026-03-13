package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RosterRepository extends JpaRepository<Roster, Integer> {

    /**
     * Find active rosters (not deleted) for a branch with date overlap
     */
    @Query("""
        SELECT r FROM Roster r
        WHERE r.branch.id = :branchId
          AND r.deleted = false
          AND (
              (r.dostartofweek <= :endDate AND r.doendofweek >= :startDate)
          )
    """)
    List<Roster> findActiveRostersByBranchAndDateRange(
            @Param("branchId") Integer branchId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Find roster by branch and exact week dates
     */
    Optional<Roster> findByBranch_IdAndDostartofweekAndDoendofweekAndDeletedFalse(
            Integer branchId,
            LocalDate dostartofweek,
            LocalDate doendofweek
    );

}
