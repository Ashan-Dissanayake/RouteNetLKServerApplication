package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `VehicleServiceStatus` entities.
 * Extends the `JpaRepository` to provide CRUD operations and custom query methods.
 */
@Repository
public interface VehicleServiceStatusRepository extends JpaRepository<VehicleServiceStatus, Integer> {

    /**
     * Finds a `VehicleServiceStatus` entity by its name.
     *
     * @param available the name of the `VehicleServiceStatus` to find.
     * @return an `Optional` containing the found `VehicleServiceStatus`, or empty if not found.
     */
    Optional<VehicleServiceStatus> findByName(String available);
}
