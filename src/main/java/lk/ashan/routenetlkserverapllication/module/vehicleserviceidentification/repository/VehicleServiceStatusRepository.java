package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicestatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServiceStatusRepository extends JpaRepository<Vehicleservicestatus, Integer> {
    Vehicleservicestatus findByName(String available);
}
