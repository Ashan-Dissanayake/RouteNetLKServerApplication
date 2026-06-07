package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleServiceExecutionRepository extends JpaRepository<VehicleServiceExecution, Integer> {
    Optional<VehicleServiceExecution> findByVehicleserviceAndDoendIsNull(VehicleService service);
}
