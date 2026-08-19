package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServicePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `VehicleServicePriority` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface VehicleServicePriorityRepository extends JpaRepository<VehicleServicePriority, Integer> {

    /**
     * Finds a `VehicleServicePriority` entity by its name.
     *
     * @param name the name of the `VehicleServicePriority` to find
     * @return an `Optional` containing the found `VehicleServicePriority`, or empty if not found
     */
    Optional<VehicleServicePriority> findByName(String name);
}
