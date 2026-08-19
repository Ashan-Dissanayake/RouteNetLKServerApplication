package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report5Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for managing Vehicle entities.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    /**
     * Checks if a vehicle exists by its number.
     *
     * @param number the vehicle number to check
     * @return true if a vehicle with the given number exists, false otherwise
     */
    boolean existsByNumber(String number);

    /**
     * Finds a vehicle by its ID.
     *
     * @param id the ID of the vehicle to find
     * @return the Vehicle entity with the given ID
     */
    @Query("select v from Vehicle v where v.id=:id")
    Vehicle findByMyId(@Param("id") Integer id);

    /**
     * Marks vehicles as deleted based on their IDs.
     *
     * @param ids the list of vehicle IDs to mark as deleted
     */
    @Modifying
    @Transactional
    @Query("UPDATE Vehicle v SET v.deleted=true WHERE v.id in :ids")
    void removeAll(@Param("ids") List<Integer> ids);

    /**
     * Finds all vehicles associated with a specific branch ID.
     *
     * @param branchId the ID of the branch
     * @return a list of vehicles associated with the given branch ID
     */
    List<Vehicle> findByBranch_Id(Integer branchId);
}
