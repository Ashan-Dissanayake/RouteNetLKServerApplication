package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing `Grn` entities.
 * Extends the `JpaRepository` to provide CRUD operations and query methods.
 */
@Repository
public interface GrnRepository extends JpaRepository<Grn, Integer> {

}
