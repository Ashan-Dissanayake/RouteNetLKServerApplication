package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripCrewAttendanceRepository extends JpaRepository<TripCrewAttendance, Integer> {
    boolean existsByTripIdAndRoleId(Integer tripId, Integer roleId);
}
