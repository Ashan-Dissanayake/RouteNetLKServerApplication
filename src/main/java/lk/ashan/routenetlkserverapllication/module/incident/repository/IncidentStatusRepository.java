package lk.ashan.routenetlkserverapllication.module.incident.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incidentstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentStatusRepository extends JpaRepository<Incidentstatus, Integer> {
    Optional<Incidentstatus> findByName(String inProgress);
}
