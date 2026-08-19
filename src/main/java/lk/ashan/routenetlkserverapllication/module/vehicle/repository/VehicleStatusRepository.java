package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `VehicleStatus` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface VehicleStatusRepository extends JpaRepository<VehicleStatus, Integer> {

    /**
     * Finds a `VehicleStatus` entity by its name.
     *
     * @param name the name of the vehicle status to search for
     * @return an `Optional` containing the found `VehicleStatus`, or empty if not found
     */
    Optional<VehicleStatus> findByName(String name);
}
