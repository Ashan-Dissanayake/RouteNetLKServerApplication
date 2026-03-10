package lk.ashan.routenetlkserverapllication.module.serviceshcedule.repository;

import lk.ashan.routenetlkserverapllication.module.serviceshcedule.model.Vehicleservicetype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServiceTypeRepository extends JpaRepository<Vehicleservicetype, Integer> {
}
