package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.CrewAttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewAttendanceStatusRepository extends JpaRepository<CrewAttendanceStatus, Integer> {
    Optional<CrewAttendanceStatus> findByName(String pending);
}
