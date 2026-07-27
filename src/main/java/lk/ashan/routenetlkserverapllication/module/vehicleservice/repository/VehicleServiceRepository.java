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
   @Query(value = "SELECT CONCAT('Week ', WEEK(vs.docreated)) AS weekLabel, " +
           "SUM(vs.vehicleservicestatus_id = 4) AS completedServices, " +   // 4 = Complete
           "SUM(vs.vehicleservicestatus_id = 1) AS pendingBacklog " +       // 1 = Pending
           "FROM vehicleservice vs " +
           "GROUP BY WEEK(vs.docreated) " +
           "ORDER BY WEEK(vs.docreated) DESC LIMIT 7", nativeQuery = true)
   List<Report3Projection> getMaintenanceLifecycleMetrics();
}
