package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing `Designation` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {

    /**
     * Finds a list of `Designation` entities by their names.
     *
     * @param driver A list of designation names to search for.
     * @return A list of `Designation` entities matching the given names.
     */
    List<Designation> findByNameIn(List<String> driver);
}
