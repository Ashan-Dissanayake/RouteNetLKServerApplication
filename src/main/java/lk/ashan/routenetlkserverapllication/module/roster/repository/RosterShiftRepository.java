package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RosterShiftRepository extends JpaRepository<RosterShift, Integer> {
    Optional<List<RosterShift>> findByRoster_Id(Integer rosterId);

    //dashboard
    @Query(value = "SELECT rs.* FROM rostershift rs " +
            "INNER JOIN roster r ON rs.roster_id = r.id " +
            "WHERE r.branch_id = :branchId " +
            "AND rs.doshift = CURRENT_DATE",
            nativeQuery = true)
    List<RosterShift> findTodayShiftsByBranchNative(@Param("branchId") Integer branchId);

}
