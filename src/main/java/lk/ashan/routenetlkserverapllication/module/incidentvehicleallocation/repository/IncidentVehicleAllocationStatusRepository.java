package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentVehicleAllocationStatusRepository extends JpaRepository<IncidentVehicleAllocationStatus, Integer> {
    Optional<IncidentVehicleAllocationStatus> findByName(String name);
}
