package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServiceStatusRepository extends JpaRepository<VehicleServiceStatus, Integer> {
    VehicleServiceStatus findByName(String available);
}
