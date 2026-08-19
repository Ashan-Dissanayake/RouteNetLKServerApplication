package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.BusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `BusType` entities.
 * Extends the Spring Data JPA `JpaRepository` to provide CRUD operations.
 */
@Repository
public interface BusTypeRepository extends JpaRepository<BusType, Integer> {

}
