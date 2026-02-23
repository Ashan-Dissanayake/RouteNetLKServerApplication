package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.Tripcrewallocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripCrewAllocationRepository extends JpaRepository<Tripcrewallocation, Integer> {
}
