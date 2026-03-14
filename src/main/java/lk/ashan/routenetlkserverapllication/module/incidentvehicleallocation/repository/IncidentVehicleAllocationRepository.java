package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentVehicleAllocationRepository extends JpaRepository<IncidentVehicleAllocation, Integer> {
    boolean existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(Integer vehicleId, List<String> statuses);
    boolean existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(Integer incidentId, Integer vehicleId, List<String> statuses);
    long countByIncident_IdAndIncidentvehicleallocationstatus_NameIn(Integer id,List<String> statuses);

    List<IncidentVehicleAllocation> findByIncident_Id(Integer id);
}
