package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeestatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("UPDATE Employee  e SET e.deleted=true WHERE e.id in :ids")
    void removeAll(@Param("ids")List<Integer>ids);

    @Modifying
    @Transactional
    @Query("UPDATE Employee  e SET e.deleted=false WHERE e.id in :ids")
    void restoreAll(@Param("ids") List<Integer> ids);

    @Query("SELECT e FROM Employee e WHERE e.designation.name = :designation AND e.driver IS NULL")
    List<Employee> findEmployeesWithoutDriver(@Param("designation") String designation);

    @Query("SELECT e FROM Employee e WHERE e.designation.name = :designation AND e.conductor IS NULL")
    List<Employee> findEmployeesWithoutConductor(@Param("designation") String designation);


    List<Employee> findByDeletedFalseAndEmployeestatus_NameAndBranch_IdAndDesignation_IdIn(
            String employeeStatus,
            Integer branchId,
            List<Integer> designationIds
    );



}
