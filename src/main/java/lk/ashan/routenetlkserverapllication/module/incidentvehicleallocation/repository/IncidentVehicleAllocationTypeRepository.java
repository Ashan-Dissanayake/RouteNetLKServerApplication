package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.Incidentvehicleallocationstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentVehicleAllocationTypeRepository extends JpaRepository<Incidentvehicleallocationstatus, Integer> {
}
