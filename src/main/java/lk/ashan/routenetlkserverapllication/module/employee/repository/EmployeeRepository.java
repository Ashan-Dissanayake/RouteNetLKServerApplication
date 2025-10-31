package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByNumber(String number);
    boolean existsByNic(String nic);
    boolean existsByMobile(String mobile);
    boolean existsByEmail(String email);
    boolean existsByEmergencycontact(String mobile );
}
