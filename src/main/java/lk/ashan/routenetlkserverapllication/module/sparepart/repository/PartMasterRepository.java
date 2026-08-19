package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partmaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `Partmaster` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface PartMasterRepository extends JpaRepository<Partmaster, Integer> {

    /**
     * Finds a `Partmaster` entity by its name.
     *
     * @param name the name of the `Partmaster` to find
     * @return an `Optional` containing the found `Partmaster`, or empty if not found
     */
    Optional<Partmaster> findByName(String name);
}
