package lk.ashan.routenetlkserverapllication.module.tripexecution.repository;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripExecutionRepository extends JpaRepository<TripExecution, Integer> {

    Optional<List<TripExecution>> findAllByTrip_Id(Integer tripId);

    List<TripExecution> findByDoserviceAndBranch_Id(LocalDate date, Integer branchId);

    List<TripExecution> findByDoserviceAndBranch_IdAndDriverIsNull(LocalDate executionDate, Integer branchId);
}
