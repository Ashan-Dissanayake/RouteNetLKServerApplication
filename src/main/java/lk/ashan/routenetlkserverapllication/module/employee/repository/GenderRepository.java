package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Gender entities.
 * Extends JpaRepository to provide CRUD operations and additional JPA functionalities.
 */
@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
}
