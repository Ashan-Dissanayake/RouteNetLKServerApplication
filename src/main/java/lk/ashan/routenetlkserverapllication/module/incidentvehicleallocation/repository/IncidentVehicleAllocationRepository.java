package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing `IncidentVehicleAllocation` entities.
 * Provides methods to check the existence of specific allocations based on various criteria.
 */
@Repository
public interface IncidentVehicleAllocationRepository extends JpaRepository<IncidentVehicleAllocation, Integer> {

    /**
     * Checks if a vehicle with the given ID exists with any of the specified statuses.
     *
     * @param vehicleId the ID of the vehicle to check
     * @param statuses the list of statuses to match
     * @return true if a vehicle with the given ID and any of the specified statuses exists, false otherwise
     */
    boolean existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(Integer vehicleId, List<String> statuses);

    /**
     * Checks if an incident and vehicle combination exists with any of the specified statuses.
     *
     * @param incidentId the ID of the incident to check
     * @param vehicleId the ID of the vehicle to check
     * @param statuses the list of statuses to match
     * @return true if the incident and vehicle combination exists with any of the specified statuses, false otherwise
     */
    boolean existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(Integer incidentId, Integer vehicleId, List<String> statuses);

    /**
     * Checks if an incident with the given ID exists with any of the specified statuses.
     *
     * @param incidentId the ID of the incident to check
     * @param assigned the list of statuses to match
     * @return true if an incident with the given ID and any of the specified statuses exists, false otherwise
     */
    boolean existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(Integer incidentId, List<String> assigned);
}
