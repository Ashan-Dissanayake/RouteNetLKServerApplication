package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicepart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServicePartRepository extends JpaRepository<Vehicleservicepart, Integer> {
}
