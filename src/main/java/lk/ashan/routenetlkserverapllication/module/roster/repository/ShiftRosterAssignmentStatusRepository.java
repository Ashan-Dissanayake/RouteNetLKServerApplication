package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignmentstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShiftRosterAssignmentStatusRepository extends JpaRepository<Shiftrosterassignmentstatus, Integer> {
    Optional<Shiftrosterassignmentstatus> findByName(String name);
}
