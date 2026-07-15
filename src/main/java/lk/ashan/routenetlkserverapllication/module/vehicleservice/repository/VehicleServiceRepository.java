package lk.ashan.routenetlkserverapllication.module.vehicleservice.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.report.model.projection.Report3Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VehicleServiceRepository extends JpaRepository<VehicleService, Integer> {
   @Query(value = "SELECT CONCAT('Week ', WEEK(vs.docreated)) as weekLabel, " +
           "SUM(CASE WHEN vs.vehicleservicestatus_id = 3 THEN 1 ELSE 0 END) as completedServices, " +
           "SUM(CASE WHEN vs.vehicleservicestatus_id = 1 THEN 1 ELSE 0 END) as pendingBacklog " +
           "FROM vehicleservice vs " +
           "GROUP BY WEEK(vs.docreated) " +
           "ORDER BY WEEK(vs.docreated) DESC LIMIT 7", nativeQuery = true)
   List<Report3Projection> getMaintenanceLifecycleMetrics();
}
