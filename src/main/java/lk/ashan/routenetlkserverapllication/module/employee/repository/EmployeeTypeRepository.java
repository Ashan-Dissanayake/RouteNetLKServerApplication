package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `EmployeeType` entities.
 * Extends the `JpaRepository` to provide CRUD operations and query methods.
 */
@Repository
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Integer> {
}
