package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing `Originterminal` entities.
 * Extends the Spring Data JPA `JpaRepository` to provide CRUD operations.
 */
@Repository
public interface OriginTerminalRepository extends JpaRepository<Originterminal, Integer> {
}
