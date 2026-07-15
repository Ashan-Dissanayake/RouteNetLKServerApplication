package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Repository interface for managing `Conductor` entities.
 * Extends the JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Integer> {

    /**
     * Finds a list of Conductors by the branch ID of their associated employee.
     *
     * @param branchId the ID of the branch to filter Conductors by
     * @return a list of Conductors associated with the specified branch ID
     */
    List<Conductor> findByEmployee_Branch_Id(Integer branchId);
}
