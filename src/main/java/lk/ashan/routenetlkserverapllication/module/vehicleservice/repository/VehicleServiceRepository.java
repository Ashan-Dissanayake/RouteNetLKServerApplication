package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report3Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository interface for managing `VehicleService` entities.
 * Provides methods for querying maintenance lifecycle metrics.
 */
@Repository
public interface VehicleServiceRepository extends JpaRepository<VehicleService, Integer> {

    /**
     * Retrieves maintenance lifecycle metrics for the last 7 weeks.
     * The metrics include the number of completed services and pending backlog services per week.
     *
     * @return A list of `Report3Projection` containing the week label, completed services, and pending backlog.
     * @throws org.springframework.dao.DataAccessException if a data access error occurs.
     */
    @Query(value = """
            SELECT
                CONCAT('Week ', week_number) AS weekLabel,
                SUM(CASE WHEN vehicleservicestatus_id = 4 THEN 1 ELSE 0 END) AS completedServices,
                SUM(CASE WHEN vehicleservicestatus_id = 1 THEN 1 ELSE 0 END) AS pendingBacklog
            FROM (
                SELECT
                    YEAR(docreated) AS year_number,
                    WEEK(docreated) AS week_number,
                    vehicleservicestatus_id
                FROM vehicleservice
                WHERE docreated IS NOT NULL
            ) AS weekly_services
            GROUP BY year_number, week_number
            ORDER BY year_number DESC, week_number DESC
            LIMIT 7
            """, nativeQuery = true)
    List<Report3Projection> getMaintenanceLifecycleMetrics();
}
