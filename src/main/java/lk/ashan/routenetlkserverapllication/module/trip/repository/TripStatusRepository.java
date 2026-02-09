package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.Triptype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripStatusRepository extends JpaRepository<Tripstatus, Integer> {
}
