package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RosterStatusRepository extends JpaRepository<Rosterstatus, Integer> {
    Optional<Rosterstatus> findByName(String draft);
}
