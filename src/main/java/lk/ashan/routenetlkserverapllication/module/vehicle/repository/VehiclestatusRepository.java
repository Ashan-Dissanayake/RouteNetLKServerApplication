package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclestatusRepository extends JpaRepository<Vehiclestatus, Integer> {
    Vehiclestatus findByName(String name);
}
