package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServicePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServicePriorityRepository extends JpaRepository<VehicleServicePriority, Integer> {
    VehicleServicePriority findByName(String critical);
}
