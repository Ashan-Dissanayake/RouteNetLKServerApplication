package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleServiceRepository extends JpaRepository<VehicleService, Integer> {

    @Query("SELECT vs.vehicle.id " +
            "FROM VehicleService vs " +
            "WHERE vs.vehicleservicestatus.name IN ('Created', 'Scheduled', 'In progress')")
    List<Integer> findVehicleIdsWithOpenServices();


    @Query("""
    SELECT CASE WHEN COUNT(vs) > 0 THEN true ELSE false END
    FROM VehicleService vs
    WHERE vs.incident.id = :incidentId
      AND vs.vehicleservicestatus.name IN ('Created', 'Scheduled', 'In progress')
""")
    boolean existsOpenServiceForIncident(@Param("incidentId") Integer incidentId);

    @Query("""
    SELECT COUNT(vs)
    FROM VehicleService vs
    WHERE vs.docreated = :date
""")
    long countByDate(@Param("date") LocalDate date);

    List<VehicleService> findByVehicleservicestatus_Name(String statusName);

}
