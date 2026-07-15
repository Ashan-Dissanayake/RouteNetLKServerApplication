package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing `Trip` entities.
 * Provides methods for querying and interacting with the `Trip` database table.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

    /**
     * Finds all trips by the origin terminal ID.
     *
     * @param originterminalId the ID of the origin terminal
     * @return a list of trips associated with the specified origin terminal
     */
    List<Trip> findByOriginterminal_Id(Integer originterminalId);

    /**
     * Checks if a trip exists with the specified permit ID, origin terminal ID, departure time,
     * arrival time, and trip status name.
     *
     * @param permitId the ID of the permit
     * @param originTerminalId the ID of the origin terminal
     * @param departure the departure time
     * @param arrival the arrival time
     * @param status the name of the trip status
     * @return true if such a trip exists, false otherwise
     */
    boolean existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
            Integer permitId, Integer originTerminalId, LocalTime departure, LocalTime arrival, String status);

    /**
     * Counts the number of trips with the specified permit ID and trip status name.
     *
     * @param permitId the ID of the permit
     * @param active the name of the trip status
     * @return the count of trips matching the criteria
     */
    long countByPermite_IdAndTripstatus_Name(Integer permitId, String active);

    /**
     * Finds all trips by the specified permit ID and trip status name.
     *
     * @param permitId the ID of the permit
     * @param active the name of the trip status
     * @return a list of trips matching the criteria
     */
    List<Trip> findByPermite_IdAndTripstatus_Name(Integer permitId, String active);

    /**
     * Counts the distinct number of permits for a shift within a specified time range.
     *
     * @param branchId the ID of the branch
     * @param startTime the start time of the shift
     * @param endTime the end time of the shift
     * @return the count of distinct permits for the shift
     */
    @Query("SELECT COUNT(DISTINCT t.permite.id) FROM Trip t " +
            "WHERE t.permite.branch.id = :branchId " +
            "AND t.tripstatus.name = 'Active' " +
            "AND t.todepature >= :startTime " +
            "AND t.todepature < :endTime")
    long countDistinctPermitsForShift(
            @Param("branchId") Integer branchId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    /**
     * Checks if an interprovincial trip exists within a specified shift time range.
     *
     * @param branchId the ID of the branch
     * @param shiftStart the start time of the shift
     * @param shiftEnd the end time of the shift
     * @return true if an interprovincial trip exists, false otherwise
     */
    @Query("SELECT COUNT(t) > 0 FROM Trip t " +
            "JOIN t.permite p " +
            "JOIN p.route r " +
            "WHERE t.branch.id = :branchId " +
            "AND r.routetype.id = 2 " + // 2 = Interprovincial/High Skill
            "AND t.todepature >= :shiftStart " +
            "AND t.todepature < :shiftEnd")
    boolean existsInterprovincialTripInShift(
            @Param("branchId") Integer branchId,
            @Param("shiftStart") LocalTime shiftStart,
            @Param("shiftEnd") LocalTime shiftEnd
    );

    /**
     * Finds all trips by the branch ID and trip status name.
     *
     * @param branchId the ID of the branch
     * @param active the name of the trip status
     * @return a list of trips matching the criteria
     */
    List<Trip> findByBranch_IdAndTripstatus_Name(Integer branchId, String active);
}
