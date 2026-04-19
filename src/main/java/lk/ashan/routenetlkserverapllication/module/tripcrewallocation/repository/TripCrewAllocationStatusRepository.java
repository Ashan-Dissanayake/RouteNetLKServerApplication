package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripCrewAllocationStatusRepository extends JpaRepository<TripCrewAllocationStatus, Integer> {
    Optional<TripCrewAllocationStatus> findByName(String confirmed);
}
