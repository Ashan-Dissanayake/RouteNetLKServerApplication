package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `VehicleServiceType` entities.
 * Provides methods for performing CRUD operations and custom queries.
 */
@Repository
public interface VehicleServiceTypeRepository extends JpaRepository<VehicleServiceType, Integer> {

    /**
     * Finds a `VehicleServiceType` entity by its name.
     *
     * @param name the name of the vehicle service type to search for
     * @return an `Optional` containing the found `VehicleServiceType`, or empty if not found
     */
    Optional<VehicleServiceType> findByName(String name);
}
