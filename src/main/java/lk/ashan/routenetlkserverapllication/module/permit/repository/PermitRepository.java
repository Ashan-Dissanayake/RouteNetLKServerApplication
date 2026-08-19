package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing `Permite` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface PermitRepository extends JpaRepository<Permite, Integer> {

    /**
     * Checks if a permit exists with the given number.
     *
     * @param number the permit number to check
     * @return true if a permit with the given number exists, false otherwise
     */
    boolean existsByNumber(String number);

    /**
     * Checks if a permit exists for a specific vehicle, route, and permit status.
     *
     * @param id the ID of the vehicle
     * @param id1 the ID of the route
     * @param activeStatusId the ID of the active permit status
     * @return true if a permit exists for the given vehicle, route, and status, false otherwise
     */
    boolean existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(Integer id, Integer id1, Integer activeStatusId);

    /**
     * Retrieves a list of permits with a specific status name that expired before a given date.
     *
     * @param name the name of the permit status
     * @param date the date before which the permits expired
     * @return a list of permits matching the criteria
     */
    List<Permite> findByPermitestatus_NameAndDoexpiredBefore(String name, LocalDate date);

    /**
     * Retrieves a list of permits with a specific status name that expired within a given date range.
     *
     * @param name the name of the permit status
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of permits matching the criteria
     */
    List<Permite> findByPermitestatus_NameAndDoexpiredBetween(String name, LocalDate startDate, LocalDate endDate);
}
