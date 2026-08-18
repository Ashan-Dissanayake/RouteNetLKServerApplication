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

@Repository
public interface TripExecutionRepository extends JpaRepository<TripExecution, Integer> {

    Optional<List<TripExecution>> findAllByTrip_Id(Integer tripId);

    List<TripExecution> findByDoserviceAndBranch_Id(LocalDate date, Integer branchId);

    List<TripExecution> findByDoserviceAndBranch_IdAndDriverIsNull(LocalDate executionDate, Integer branchId);

    List<TripExecution> findByTripexecutionstatus_Name(String status);

    //Report-3
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

    //Dashboard
    @Query("SELECT COUNT(te) FROM TripExecution te " +
            "WHERE te.branch.id = :branchId " +
            "AND te.doservice = CURRENT_DATE " +
            "AND te.tripexecutionstatus.name = 'Dispatched'")
    long countActiveTripsByBranch(@Param("branchId") Integer branchId);
}
