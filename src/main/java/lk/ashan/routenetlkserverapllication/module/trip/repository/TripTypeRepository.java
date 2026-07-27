package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Triptype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `Triptype` entities.
 * Extends the Spring Data JPA `JpaRepository` to provide CRUD operations.
 */
@Repository
public interface TripTypeRepository extends JpaRepository<Triptype, Integer> {
}
