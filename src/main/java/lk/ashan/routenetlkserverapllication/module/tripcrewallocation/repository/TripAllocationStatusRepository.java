package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripallocationstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripAllocationStatusRepository extends JpaRepository<Tripallocationstatus, Integer> {
    Optional<Tripallocationstatus> findByName(String confirmed);
}
