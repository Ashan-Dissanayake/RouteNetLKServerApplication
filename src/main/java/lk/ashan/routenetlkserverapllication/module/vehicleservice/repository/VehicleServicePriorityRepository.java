package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServicePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleServicePriorityRepository extends JpaRepository<VehicleServicePriority, Integer> {
    Optional<VehicleServicePriority> findByName(String name);
}
