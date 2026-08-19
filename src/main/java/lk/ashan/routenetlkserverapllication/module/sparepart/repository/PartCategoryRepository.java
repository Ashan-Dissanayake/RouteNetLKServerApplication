package lk.ashan.routenetlkserverapllication.module.sparepart.repository;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `Partcategory` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface PartCategoryRepository extends JpaRepository<Partcategory, Integer> {

    /**
     * Finds a `Partcategory` entity by its name.
     *
     * @param name the name of the part category to search for
     * @return an `Optional` containing the found `Partcategory`, or empty if not found
     */
    Optional<Partcategory> findByName(String name);
}
