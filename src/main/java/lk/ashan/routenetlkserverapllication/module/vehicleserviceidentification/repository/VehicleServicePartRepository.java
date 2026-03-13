package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicepart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleServicePartRepository extends JpaRepository<Vehicleservicepart, Integer> {
}
