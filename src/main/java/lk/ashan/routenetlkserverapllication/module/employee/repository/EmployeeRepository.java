package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeestatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByNumber(String number);
    boolean existsByNic(String nic);
    boolean existsByMobile(String mobile);
    boolean existsByEmail(String email);
    boolean existsByEmergencycontact(String mobile );

    boolean existsByNumberAndIdNot(String number, Integer id);

    boolean existsByNicAndIdNot(String nic, Integer employeeId);

    boolean existsByMobileAndIdNot(String mobile, Integer employeeId);

    boolean existsByEmailAndIdNot(String email, Integer employeeId);

    boolean existsByEmergencycontactAndIdNot(String mobile, Integer employeeId);

    @Query("select e from Employee e where e.id=:id")
    Employee findByMyId(@Param("id")Integer id);

}
