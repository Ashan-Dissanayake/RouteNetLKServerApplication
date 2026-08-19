package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Unitofmeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing `Unitofmeasure` entities.
 * Extends the JpaRepository interface to provide CRUD operations.
 */
@Repository
public interface UnitOfMeasureRepository extends JpaRepository<Unitofmeasure, Integer> {
}
