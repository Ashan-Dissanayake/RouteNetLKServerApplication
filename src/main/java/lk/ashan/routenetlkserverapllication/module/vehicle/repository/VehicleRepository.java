package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    boolean existsByCode(String code);
    boolean existsByNumber(String number);
    boolean existsByChasisnumber(String chasisnumber);
    boolean existsByEnginenumber(String enginenumber);
    boolean existsByCodeOrChasisnumber(String code, String chasisnumber);
    boolean existsByCodeOrEnginenumber(String code, String enginenumber);


}
