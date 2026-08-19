package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Shift entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    /**
     * Finds a list of Shift entities by the name of their associated shift status.
     *
     * @param active the name of the shift status to filter by.
     * @return a list of Shift entities matching the specified shift status name.
     */
    List<Shift> findByShiftstatus_Name(String active);
}
