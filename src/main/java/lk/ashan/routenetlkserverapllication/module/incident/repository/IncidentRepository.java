package lk.ashan.routenetlkserverapllication.module.incident.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report1Projection;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report5Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {
    // --- REPORT 1: Fleet Dispatch & Breakdown Proportions ---
    // Query to count successful dispatches per weekday
    @Query(value = "SELECT WEEKDAY(doservice) as dayIdx, COUNT(id) as trips " +
            "FROM tripexecution " +
            "WHERE tripexecutionstatus_id = :statusId " +
            "GROUP BY WEEKDAY(doservice)", nativeQuery = true)
    List<Object[]> getTripsCountByDay(@Param("statusId") Integer statusId);

    // Query to count breakdowns per weekday
    @Query(value = "SELECT WEEKDAY(doreported) as dayIdx, COUNT(id) as incidents " +
            "FROM incident " +
            "WHERE incidenttype_id = :typeId " +
            "GROUP BY WEEKDAY(doreported)", nativeQuery = true)
    List<Object[]> getIncidentsCountByDay(@Param("typeId") Integer typeId);

    // --- REPORT 5: Proportional Distribution of Fleet Route Incidents ---
    @Query(value = "SELECT it.name as incidentTypeName, COUNT(i.id) as incidentCount " +
            "FROM incident i " +
            "JOIN incidenttype it ON i.incidenttype_id = it.id " +
            "GROUP BY it.id, it.name", nativeQuery = true)
    List<Report5Projection> getIncidentDistributionMetrics();


    //dashboard
    @Query("SELECT i FROM Incident i " +
            "WHERE i.tripexecution.branch.id = :branchId " +
            "AND i.incidentstatus.name != 'Resolved' " +
            "ORDER BY i.id DESC")
    List<Incident> findActiveIncidentsByBranch(@Param("branchId") Integer branchId);

    @Query("SELECT COUNT(i) FROM Incident i " +
            "WHERE i.tripexecution.branch.id = :branchId " +
            "AND i.incidentstatus.name = 'Pending'")
    long countPendingIncidentsByBranch(@Param("branchId") Integer branchId);
}
