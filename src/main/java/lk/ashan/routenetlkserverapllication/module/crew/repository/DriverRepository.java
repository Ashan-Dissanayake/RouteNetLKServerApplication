package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing `Driver` entities.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    /**
     * Checks if a driver exists with the given license number.
     *
     * @param licenseNumber the license number to check
     * @return true if a driver exists with the given license number, false otherwise
     */
    boolean existsByLicensenumber(String licenseNumber);

    /**
     * Checks if a driver exists with the given number.
     *
     * @param number the number to check
     * @return true if a driver exists with the given number, false otherwise
     */
    boolean existsByNumber(String number);

    /**
     * Checks if a driver exists with the given license number, excluding a specific driver ID.
     *
     * @param licenseNumber the license number to check
     * @param id the ID of the driver to exclude from the check
     * @return true if a driver exists with the given license number and a different ID, false otherwise
     */
    boolean existsByLicensenumberAndIdNot(String licenseNumber, Integer id);

    /**
     * Checks if a driver exists with the given number, excluding a specific driver ID.
     *
     * @param number the number to check
     * @param id the ID of the driver to exclude from the check
     * @return true if a driver exists with the given number and a different ID, false otherwise
     */
    boolean existsByNumberAndIdNot(String number, Integer id);

    /**
     * Retrieves a list of drivers whose employee IDs are in the given list.
     *
     * @param ids the list of employee IDs to filter by
     * @return a list of drivers matching the given employee IDs
     */
    @Query("SELECT d FROM Driver d WHERE d.employee.id IN :ids")
    List<Driver> findAllByEmployeeIds(@Param("ids") List<Integer> ids);

    /**
     * Retrieves a list of drivers associated with a specific branch ID.
     *
     * @param branchId the ID of the branch to filter by
     * @return a list of drivers associated with the given branch ID
     */
    @Query("SELECT d FROM Driver d WHERE d.employee.branch.id = :branchId")
    List<Driver> findByEmployee_Branch_Id(@Param("branchId") Integer branchId);


    //dashboard
    @Query("SELECT COUNT(d) FROM Driver d " +
            "WHERE d.employee.branch.id = :branchId " +
            "AND d.crewstatus.name = 'Eligible'")
    long countStandbyDriversByBranch(@Param("branchId") Integer branchId);

    boolean existsByEmployeeId(Integer employeeId);

    boolean existsByEmployeeIdAndIdNot(Integer employeeId, Integer id);

    Optional<Driver> findByEmployeeId(Integer id);



    @Query("""
    SELECT d
    FROM Driver d
    JOIN FETCH d.employee e
    WHERE e.branch.id = :branchId
    AND e.employeestatus.name = 'Active'
    """)
    List<Driver> findAvailableDrivers(@Param("branchId") Integer branchId);
}
