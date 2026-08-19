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

/**
 * Repository interface for managing `Incident` entities.
 * Provides methods for querying incident-related data for reports and dashboards.
 */
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    // --- REPORT 1: Fleet Dispatch & Breakdown Proportions ---

    /**
     * Retrieves the count of trips grouped by the day of the week for a specific trip execution status.
     *
     * @param statusId the ID of the trip execution status to filter by
     * @return a list of objects where each object contains the day index (0-6) and the trip count
     */
    @Query(value = """
            SELECT WEEKDAY(doservice) AS dayIdx,
                   COUNT(id) AS trips
            FROM tripexecution
            WHERE tripexecutionstatus_id = :statusId
            GROUP BY WEEKDAY(doservice)
            """, nativeQuery = true)
    List<Object[]> getTripsCountByDay(@Param("statusId") Integer statusId);

    /**
     * Retrieves the count of incidents grouped by the day of the week for a specific incident type.
     *
     * @param typeId the ID of the incident type to filter by
     * @return a list of objects where each object contains the day index (0-6) and the incident count
     */
    @Query(value = """
            SELECT WEEKDAY(doreported) AS dayIdx,
                   COUNT(id) AS incidents
            FROM incident
            WHERE incidenttype_id = :typeId
            GROUP BY WEEKDAY(doreported)
            """, nativeQuery = true)
    List<Object[]> getIncidentsCountByDay(@Param("typeId") Integer typeId);

    // --- REPORT 5: Proportional Distribution of Fleet Route Incidents ---

    /**
     * Retrieves the proportional distribution of incidents by incident type.
     *
     * @return a list of `Report5Projection` objects containing the incident type name and the incident count
     */
    @Query(value = """
            SELECT it.name AS incidentTypeName,
                   COUNT(i.id) AS incidentCount
            FROM incident i
            JOIN incidenttype it ON i.incidenttype_id = it.id
            GROUP BY it.id, it.name
            """, nativeQuery = true)
    List<Report5Projection> getIncidentDistributionMetrics();

    // --- Dashboard ---

    /**
     * Retrieves a list of active incidents for a specific branch, excluding resolved incidents.
     *
     * @param branchId the ID of the branch to filter by
     * @return a list of `Incident` objects sorted in descending order by ID
     */
    @Query("""
            SELECT i FROM Incident i
            WHERE i.tripexecution.branch.id = :branchId
            AND i.incidentstatus.name != 'Resolved'
            ORDER BY i.id DESC
            """)
    List<Incident> findActiveIncidentsByBranch(@Param("branchId") Integer branchId);

    /**
     * Counts the number of pending incidents for a specific branch.
     *
     * @param branchId the ID of the branch to filter by
     * @return the count of pending incidents
     */
    @Query("""
            SELECT COUNT(i) FROM Incident i
            WHERE i.tripexecution.branch.id = :branchId
            AND i.incidentstatus.name = 'Pending Allocation'
            """)
    long countPendingIncidentsByBranch(@Param("branchId") Integer branchId);
}
