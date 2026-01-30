package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RosterAssignmentStatusRepository extends JpaRepository<Rosterassignementstatus, Integer> {
    Rosterassignementstatus findByName(String name);
}
