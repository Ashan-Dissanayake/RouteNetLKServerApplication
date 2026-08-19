package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing IncidentVehicleAllocationStatus entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface IncidentVehicleAllocationStatusRepository extends JpaRepository<IncidentVehicleAllocationStatus, Integer> {

    /**
     * Finds an IncidentVehicleAllocationStatus entity by its name.
     *
     * @param name the name of the IncidentVehicleAllocationStatus to find
     * @return an Optional containing the found IncidentVehicleAllocationStatus, or empty if not found
     */
    Optional<IncidentVehicleAllocationStatus> findByName(String name);
}
