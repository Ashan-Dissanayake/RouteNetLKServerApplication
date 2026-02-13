package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.Overridestatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverrideStatusRepository extends JpaRepository<Overridestatus, Integer> {
    Optional<Overridestatus> findByName(String name);
}
