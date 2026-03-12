package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface VehicleServiceRepository extends JpaRepository<Vehicleservice, Integer> {

    @Query("SELECT vs.vehicle.id " +
            "FROM Vehicleservice vs " +
            "WHERE vs.vehicleservicestatus.name IN ('Created', 'Scheduled', 'In progress')")
    List<Integer> findVehicleIdsWithOpenServices();

    @Query("select vs.lastservicemileage from Vehicleservice vs where vs.vehicle.id=:id")
    Integer findLastServiceMileage(@Param("id") Integer id);


    @Query("""
    SELECT CASE WHEN COUNT(vs) > 0 THEN true ELSE false END
    FROM Vehicleservice vs
    WHERE vs.incident.id = :incidentId
      AND vs.vehicleservicestatus.name IN ('Created', 'Scheduled', 'In progress')
""")
    boolean existsOpenServiceForIncident(@Param("incidentId") Integer incidentId);

    @Query("""
    SELECT COUNT(vs)
    FROM Vehicleservice vs
    WHERE vs.docreated = :date
""")
    long countByDate(@Param("date") LocalDate date);

    List<Vehicleservice> findByVehicleservicestatus_Name(String statusName);

}
