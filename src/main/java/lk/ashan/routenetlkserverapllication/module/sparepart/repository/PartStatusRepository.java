package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `Partstatus` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface PartStatusRepository extends JpaRepository<Partstatus, Integer> {

    /**
     * Finds a `Partstatus` entity by its name.
     *
     * @param name the name of the part status to search for
     * @return an `Optional` containing the found `Partstatus`, or empty if not found
     */
    Optional<Partstatus> findByName(String name);
}
