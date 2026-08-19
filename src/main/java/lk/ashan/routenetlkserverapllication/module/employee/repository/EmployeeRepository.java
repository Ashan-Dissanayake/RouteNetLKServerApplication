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

/**
 * Repository interface for managing Employee entities.
 * Provides methods for performing CRUD operations and custom queries.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Checks if an employee exists with the given NIC.
     *
     * @param nic the NIC of the employee
     * @return true if an employee exists with the given NIC, false otherwise
     */
    boolean existsByNic(String nic);

    /**
     * Checks if an employee exists with the given mobile number.
     *
     * @param mobile the mobile number of the employee
     * @return true if an employee exists with the given mobile number, false otherwise
     */
    boolean existsByMobile(String mobile);

    /**
     * Checks if an employee exists with the given emergency contact number.
     *
     * @param mobile the emergency contact number of the employee
     * @return true if an employee exists with the given emergency contact number, false otherwise
     */
    boolean existsByEmergencycontact(String mobile);

    /**
     * Checks if an employee exists with the given NIC, excluding the specified employee ID.
     *
     * @param nic the NIC of the employee
     * @param employeeId the ID of the employee to exclude
     * @return true if an employee exists with the given NIC, false otherwise
     */
    boolean existsByNicAndIdNot(String nic, Integer employeeId);

    /**
     * Checks if an employee exists with the given mobile number, excluding the specified employee ID.
     *
     * @param mobile the mobile number of the employee
     * @param employeeId the ID of the employee to exclude
     * @return true if an employee exists with the given mobile number, false otherwise
     */
    boolean existsByMobileAndIdNot(String mobile, Integer employeeId);

    /**
     * Checks if an employee exists with the given emergency contact number, excluding the specified employee ID.
     *
     * @param mobile the emergency contact number of the employee
     * @param employeeId the ID of the employee to exclude
     * @return true if an employee exists with the given emergency contact number, false otherwise
     */
    boolean existsByEmergencycontactAndIdNot(String mobile, Integer employeeId);

    /**
     * Marks employees as deleted based on the given list of IDs.
     *
     * @param ids the list of employee IDs to mark as deleted
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deleted=true WHERE e.id in :ids")
    void removeAll(@Param("ids") List<Integer> ids);

    /**
     * Restores employees by unmarking them as deleted based on the given list of IDs.
     *
     * @param ids the list of employee IDs to restore
     */
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.deleted=false WHERE e.id in :ids")
    void restoreAll(@Param("ids") List<Integer> ids);

    /**
     * Finds employees with the specified designation who do not have a driver assigned.
     *
     * @param designation the designation of the employees
     * @return a list of employees without a driver
     */
    @Query("SELECT e FROM Employee e WHERE e.designation.name = :designation AND e.driver IS NULL")
    List<Employee> findEmployeesWithoutDriver(@Param("designation") String designation);

    /**
     * Finds employees with the specified designation who do not have a conductor assigned.
     *
     * @param designation the designation of the employees
     * @return a list of employees without a conductor
     */
    @Query("SELECT e FROM Employee e WHERE e.designation.name = :designation AND e.conductor IS NULL")
    List<Employee> findEmployeesWithoutConductor(@Param("designation") String designation);

    /**
     * Finds active employees with their familiarity level based on the branch ID and designation IDs.
     *
     * @param branchId the ID of the branch
     * @param designationIds the list of designation IDs
     * @return a list of projections containing employee details and familiarity level
     */
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
