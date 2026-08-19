package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `Department` entities.
 * Extends the JpaRepository interface to provide CRUD operations
 * and additional query method support for the `Department` entity.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
