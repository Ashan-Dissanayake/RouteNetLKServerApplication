package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    boolean existsByCode(String code);
    boolean existsByNumber(String number);
    boolean existsByChasisnumber(String chasisnumber);
    boolean existsByEnginenumber(String enginenumber);
    boolean existsByCodeOrChasisnumber(String code, String chasisnumber);
    boolean existsByCodeOrEnginenumber(String code, String enginenumber);

    @Query("select v from Vehicle v where v.id=:id")
    Vehicle findByMyId(@Param("id")Integer id);


}
