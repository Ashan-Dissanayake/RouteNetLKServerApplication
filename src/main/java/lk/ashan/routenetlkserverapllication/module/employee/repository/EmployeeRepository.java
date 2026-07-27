package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.projection.EmployeeFamiliarityProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByNic(String nic);
    boolean existsByMobile(String mobile);
    boolean existsByEmergencycontact(String mobile );
    boolean existsByNicAndIdNot(String nic, Integer employeeId);
    boolean existsByMobileAndIdNot(String mobile, Integer employeeId);
    boolean existsByEmergencycontactAndIdNot(String mobile, Integer employeeId);


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

    @Query("SELECT e.id as id, e.fullname as fullname, e.designation.id as designationId, " +
            "COALESCE(d.routefamiliaritylevel.id, c.routefamiliaritylevel.id) as familiarityLevel " +
            "FROM Employee e " +
            "LEFT JOIN Driver d ON e.id = d.employee.id " +
            "LEFT JOIN Conductor c ON e.id = c.employee.id " +
            "WHERE e.branch.id = :branchId " +
            "AND e.employeestatus.name = 'Active' " +
            "AND e.designation.id IN :designationIds")
    List<EmployeeFamiliarityProjection> findActiveEmployeesWithFamiliarity(
            @Param("branchId") Integer branchId,
            @Param("designationIds") List<Integer> designationIds
    );

}
