package lk.ashan.routenetlkserverapllication.module.driver.repository;

import lk.ashan.routenetlkserverapllication.module.driver.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    boolean existsByLicensenumber(String licenseNumber);
    boolean existsByNumber(String number);
}
