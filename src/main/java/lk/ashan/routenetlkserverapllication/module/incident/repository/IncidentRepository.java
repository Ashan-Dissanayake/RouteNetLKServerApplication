package lk.ashan.routenetlkserverapllication.module.incident.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report1Projection;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report5Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {
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

    // --- REPORT 5: Proportional Distribution of Fleet Route Incidents ---
    @Query(value = "SELECT it.name as incidentTypeName, COUNT(i.id) as incidentCount " +
            "FROM incident i " +
            "JOIN incidenttype it ON i.incidenttype_id = it.id " +
            "GROUP BY it.id, it.name", nativeQuery = true)
    List<Report5Projection> getIncidentDistributionMetrics();
}
