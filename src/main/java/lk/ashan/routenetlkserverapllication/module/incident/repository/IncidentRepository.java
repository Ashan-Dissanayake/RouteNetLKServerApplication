package lk.ashan.routenetlkserverapllication.module.incident.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {
    boolean existsByTrip_IdAndIncidentstatus_NameIn(Integer tripId, List<String> statuses);

    Optional<Incident> findLatestIncidentByTrip_Permite_Vehicle_Id(Integer id);
}
