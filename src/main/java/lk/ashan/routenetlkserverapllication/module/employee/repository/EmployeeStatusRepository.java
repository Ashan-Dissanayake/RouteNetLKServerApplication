package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing EmployeeStatus entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Integer> {

    /**
     * Finds an EmployeeStatus entity by its name.
     *
     * @param name the name of the EmployeeStatus to find
     * @return an Optional containing the EmployeeStatus if found, or empty if not found
     */
    Optional<EmployeeStatus> findByName(String name);
}
