package lk.ashan.routenetlkserverapllication.module.tripexecution.repository;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report1Projection;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report4Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Struct;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing TripExecution entities.
 * Provides methods for querying and retrieving TripExecution data.
 */
@Repository
public interface TripExecutionRepository extends JpaRepository<TripExecution, Integer> {

    /**
     * Finds all TripExecution records associated with a specific trip ID.
     *
     * @param tripId the ID of the trip
     * @return an Optional containing a list of TripExecution records, or an empty Optional if none are found
     */
    Optional<List<TripExecution>> findAllByTrip_Id(Integer tripId);

    /**
     * Finds TripExecution records by date of service and branch ID.
     *
     * @param date the date of service
     * @param branchId the ID of the branch
     * @return a list of TripExecution records matching the criteria
     */
    List<TripExecution> findByDoserviceAndBranch_Id(LocalDate date, Integer branchId);

    /**
     * Finds TripExecution records by date of service, branch ID, and where the driver is null.
     *
     * @param executionDate the date of service
     * @param branchId the ID of the branch
     * @return a list of TripExecution records matching the criteria
     */
    List<TripExecution> findByDoserviceAndBranch_IdAndDriverIsNull(LocalDate executionDate, Integer branchId);

    /**
     * Finds TripExecution records by the name of their execution status.
     *
     * @param status the name of the trip execution status
     * @return a list of TripExecution records matching the status
     */
    List<TripExecution> findByTripexecutionstatus_Name(String status);

    /**
     * Retrieves dynamic performance metrics for a specified date range.
     * The metrics include log date, total passengers, and total distance.
     *
     * @param start the start date of the range
     * @param end the end date of the range
     * @return a list of Report4Projection containing the performance metrics
     */
    @Query(value = "SELECT DATE_FORMAT(t.doservice, '%b %d') as logDate, " +
            "SUM(t.passengercount) as totalPassengers, " +
            "SUM(CAST(t.endodometer AS SIGNED) - CAST(t.startodometer AS SIGNED)) as totalDistance " +
            "FROM tripexecution t " +
            "WHERE t.doservice BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(t.doservice) " +
            "ORDER BY DATE(t.doservice)", nativeQuery = true)
    List<Report4Projection> getDynamicPerformanceMetrics(
            @Param("startDate") Date start,
            @Param("endDate") Date end
    );

    /**
     * Counts the number of active trips for a specific branch on the current date.
     * Active trips are those with the status 'Dispatched'.
     *
     * @param branchId the ID of the branch
     * @return the count of active trips for the branch
     */
    @Query("SELECT COUNT(te) FROM TripExecution te " +
            "WHERE te.branch.id = :branchId " +
            "AND te.doservice = CURRENT_DATE " +
            "AND te.tripexecutionstatus.name = 'Dispatched'")
    long countActiveTripsByBranch(@Param("branchId") Integer branchId);
}
