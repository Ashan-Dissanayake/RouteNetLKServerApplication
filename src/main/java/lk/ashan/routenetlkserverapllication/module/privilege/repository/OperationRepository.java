package lk.ashan.routenetlkserverapllication.module.privilege.repository;

import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `Operation` entities.
 * Extends the Spring Data JPA `JpaRepository` to provide CRUD operations.
 */
@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
