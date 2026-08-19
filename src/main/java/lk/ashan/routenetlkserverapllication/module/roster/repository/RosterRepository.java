package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Repository interface for managing `Roster` entities.
 * Extends the `JpaRepository` to provide CRUD operations and custom queries.
 */
@Repository
public interface RosterRepository extends JpaRepository<Roster, Integer> {

    /**
     * Checks if there is an overlapping roster for a specific branch within the given date range.
     *
     * @param branchId  the ID of the branch to check for overlapping rosters
     * @param startDate the start date of the range to check
     * @param endDate   the end date of the range to check
     * @return `true` if an overlapping roster exists, `false` otherwise
     */
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
