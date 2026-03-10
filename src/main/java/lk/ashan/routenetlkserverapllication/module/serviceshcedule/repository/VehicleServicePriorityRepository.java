package lk.ashan.routenetlkserverapllication.module.serviceshcedule.repository;

import lk.ashan.routenetlkserverapllication.module.serviceshcedule.model.Vehicleservicepriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServicePriorityRepository extends JpaRepository<Vehicleservicepriority, Integer> {
}
