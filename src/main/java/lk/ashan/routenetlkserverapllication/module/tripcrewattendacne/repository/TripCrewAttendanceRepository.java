package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Tripcrewattendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripCrewAttendanceRepository extends JpaRepository<Tripcrewattendance, Integer> {
    boolean existsByTripIdAndRoleId(Integer tripId, Integer roleId);
}
