package lk.ashan.routenetlkserverapllication.module.tripexecution.repository;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripExecutionStatusRepository extends JpaRepository<TripExecutionStatus, Integer> {
    Optional<TripExecutionStatus> findByName(String scheduled);
}
