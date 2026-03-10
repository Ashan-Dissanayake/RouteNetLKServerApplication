package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicepriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServicePriorityRepository extends JpaRepository<Vehicleservicepriority, Integer> {
    Vehicleservicepriority findByName(String critical);
}
