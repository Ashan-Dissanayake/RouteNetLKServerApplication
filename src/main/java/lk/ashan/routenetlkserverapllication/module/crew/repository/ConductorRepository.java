package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing `Conductor` entities.
 * Extends the JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Integer> {

    /**
     * Finds a list of Conductors by the branch ID of their associated employee.
     *
     * @param branchId the ID of the branch to filter Conductors by.
     * @return a list of Conductors associated with the specified branch ID.
     */
    @Query("SELECT c FROM Conductor c WHERE c.employee.branch.id = :branchId")
    List<Conductor> findByEmployee_Branch_Id(@Param("branchId") Integer branchId);

    /**
     * Counts the number of standby Conductors in a specific branch.
     *
     * @param branchId the ID of the branch to filter Conductors by.
     * @return the count of standby Conductors associated with the specified branch ID.
     */
    @Query("SELECT COUNT(c) FROM Conductor c " +
            "WHERE c.employee.branch.id = :branchId " +
            "AND c.crewstatus.name = 'Eligible'")
    long countStandbyConductorsByBranch(@Param("branchId") Integer branchId);

    /**
     * Checks if a Conductor exists by their employee ID.
     *
     * @param employeeId the ID of the employee to check.
     * @return true if a Conductor with the specified employee ID exists, false otherwise.
     */
    boolean existsByEmployeeId(Integer employeeId);

    /**
     * Checks if a Conductor exists by their employee ID, excluding a specific Conductor ID.
     *
     * @param employeeId the ID of the employee to check.
     * @param id the ID of the Conductor to exclude from the check.
     * @return true if a Conductor with the specified employee ID exists, false otherwise.
     */
    boolean existsByEmployeeIdAndIdNot(Integer employeeId, Integer id);

    /**
     * Finds a Conductor by their employee ID.
     *
     * @param id the ID of the employee to find.
     * @return an Optional containing the Conductor if found, or an empty Optional otherwise.
     */
    Optional<Conductor> findByEmployeeId(Integer id);

    /**
     * Finds a list of available Conductors in a specific branch.
     * Conductors are considered available if their employee status is 'Active'.
     *
     * @param branchId the ID of the branch to filter Conductors by.
     * @return a list of available Conductors associated with the specified branch ID.
     */
    @Query("""
    SELECT c
    FROM Conductor c
    JOIN FETCH c.employee e
    WHERE e.branch.id = :branchId
    AND e.employeestatus.name = 'Active'
    """)
    List<Conductor> findAvailableConductors(@Param("branchId") Integer branchId);
}
