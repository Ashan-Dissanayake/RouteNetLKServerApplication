package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRosterAssignmentRepository extends JpaRepository<Shiftrosterassignment, Integer> {
    List<Shiftrosterassignment> findByRoster_Id(Integer rosterId);

    List<Shiftrosterassignment> findByRoster_IdAndShiftrosterassignmentstatus_Name(Integer rosterId, String suggested);

}
