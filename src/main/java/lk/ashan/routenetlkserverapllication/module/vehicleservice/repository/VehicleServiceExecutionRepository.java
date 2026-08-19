package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `VehicleServiceExecution` entities.
 * Extends the `JpaRepository` to provide CRUD operations and custom query methods.
 */
@Repository
public interface VehicleServiceExecutionRepository extends JpaRepository<VehicleServiceExecution, Integer> {

    /**
     * Finds a `VehicleServiceExecution` entity by the associated `VehicleService`
     * where the `doEnd` field is null.
     *
     * @param service the `VehicleService` entity to search for.
     * @return an `Optional` containing the `VehicleServiceExecution` entity if found, or empty if not found.
     */
    Optional<VehicleServiceExecution> findByVehicleserviceAndDoendIsNull(VehicleService service);
}
