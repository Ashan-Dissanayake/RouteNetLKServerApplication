package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RosterAssignmentRepository extends JpaRepository<Rosterassignement, Integer> {

}
