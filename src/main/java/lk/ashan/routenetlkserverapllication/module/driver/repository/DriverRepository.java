package lk.ashan.routenetlkserverapllication.module.driver.repository;

import lk.ashan.routenetlkserverapllication.module.driver.model.Driver;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    boolean existsByLicensenumber(String licenseNumber);
    boolean existsByNumber(String number);

    boolean existsByLicensenumberAndIdNot(String licenseNumber, Integer id);

    boolean existsByNumberAndIdNot(String number, Integer id);


    @Query("select d from Driver d where d.id=:id")
    Driver findByMyId(@Param("id")Integer id);
}
