package lk.ashan.routenetlkserverapllication.module.privilege.repository;

import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `Module` entities.
 * Extends JpaRepository to provide CRUD operations and query methods.
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
}
