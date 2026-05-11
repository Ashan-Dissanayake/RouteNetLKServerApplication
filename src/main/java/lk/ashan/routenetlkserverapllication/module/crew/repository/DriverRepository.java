package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    boolean existsByLicensenumber(String licenseNumber);
    boolean existsByNumber(String number);

    boolean existsByLicensenumberAndIdNot(String licenseNumber, Integer id);

    boolean existsByNumberAndIdNot(String number, Integer id);

    @Query("SELECT d FROM Driver d WHERE d.employee.id IN :ids")
    List<Driver> findAllByEmployeeIds(@Param("ids") List<Integer> ids);

    List<Driver> findByEmployee_Branch_Id(Integer branchId);
}
