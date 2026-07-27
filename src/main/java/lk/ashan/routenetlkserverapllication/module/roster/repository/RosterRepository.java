package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RosterRepository extends JpaRepository<Roster, Integer> {
    @Query("SELECT COUNT(r) > 0 FROM Roster r " +
            "WHERE r.branch.id = :branchId " +
            "AND r.deleted = false " +
            "AND r.dostartofweek <= :endDate " +
            "AND r.doendofweek >= :startDate")
    boolean existsByBranchIdAndDateRange(
            @Param("branchId") Integer branchId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
FROM Roster r
WHERE r.branch.id = :branchId
AND r.dostartofweek <= :endDate
AND r.doendofweek >= :startDate
""")
    boolean existsOverlappingRoster(
            @Param("branchId") Integer branchId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
