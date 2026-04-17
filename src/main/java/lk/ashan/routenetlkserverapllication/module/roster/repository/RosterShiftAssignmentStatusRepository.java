package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RosterShiftAssignmentStatusRepository extends JpaRepository<RosterShiftAssignmentStatus, Integer> {
    Optional<RosterShiftAssignmentStatus> findByName(String name);
}
