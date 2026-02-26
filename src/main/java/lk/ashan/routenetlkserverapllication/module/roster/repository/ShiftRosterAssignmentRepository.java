package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRosterAssignmentRepository extends JpaRepository<Shiftrosterassignment, Integer> {
    List<Shiftrosterassignment> findByRoster_Id(Integer rosterId);

    List<Shiftrosterassignment> findByRoster_IdAndShiftrosterassignmentstatus_Name(Integer rosterId, String suggested);

    /**
     * Find confirmed roster assignments for a specific shift and date.
     * Used by Trip Crew Allocation to load candidate employees.
     */
    List<Shiftrosterassignment> findByShift_IdAndDoassignedAndShiftrosterassignmentstatus_Name(
            Integer shiftId,
            LocalDate doassigned,
            String statusName
    );
}
