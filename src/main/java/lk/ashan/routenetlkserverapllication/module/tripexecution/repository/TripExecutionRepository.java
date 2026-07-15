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

    // --- REPORT 1: Fleet Dispatch & Breakdown Proportions ---
    @Query(value = "SELECT EL.day_name as dayName, " +
            "COALESCE(T.trips, 0) as successfulTrips, " +
            "COALESCE(I.incidents, 0) as breakdownCount " +
            "FROM (SELECT 'Monday' as day_name, 1 as idx UNION SELECT 'Tuesday', 2 UNION SELECT 'Wednesday', 3 " +
            "      UNION SELECT 'Thursday', 4 UNION SELECT 'Friday', 5 UNION SELECT 'Saturday', 6 UNION SELECT 'Sunday', 7) EL " +
            "LEFT JOIN (SELECT DAYNAME(doservice) as d_name, COUNT(id) as trips FROM tripexecution " +
            "           WHERE tripexecutionstatus_id = 3 GROUP BY DAYNAME(doservice)) T ON EL.day_name = T.d_name " +
            "LEFT JOIN (SELECT DAYNAME(doreported) as d_name, COUNT(id) as incidents FROM incident " +
            "           WHERE incidenttype_id = 1 GROUP BY DAYNAME(doreported)) I ON EL.day_name = I.d_name " +
            "ORDER BY EL.idx", nativeQuery = true)
    List<Report1Projection> getFleetDispatchAndBreakdownMetrics();

    @Query(value = "SELECT DATE_FORMAT(t.doservice, '%b %d') as logDate, " +
            "SUM(t.passengercount) as totalPassengers, " +
            "SUM(CAST(t.endodometer AS SIGNED) - CAST(t.startodometer AS SIGNED)) as totalDistance " +
            "FROM tripexecution t " +
            "WHERE t.doservice BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(t.doservice) " +
            "ORDER BY DATE(t.doservice)", nativeQuery = true)
    List<Report4Projection> getDynamicPerformanceMetrics(@Param("startDate") Date start, @Param("endDate") Date end);
}
