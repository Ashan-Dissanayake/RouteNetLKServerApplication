package lk.ashan.routenetlkserverapllication.module.serviceshcedule.repository;

import lk.ashan.routenetlkserverapllication.module.serviceshcedule.model.Vehicleservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleServiceRepository extends JpaRepository<Vehicleservice, Integer> {
}
