package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleServiceStatusRepository extends JpaRepository<VehicleServiceStatus, Integer> {
    Optional<VehicleServiceStatus> findByName(String available);
}
