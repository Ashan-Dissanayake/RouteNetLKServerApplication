package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleserviceschedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface VehicleServiceScheduleRepository extends JpaRepository<Vehicleserviceschedule, Integer> {
    @Query("""
SELECT MAX(s.doscheduledend)
FROM Vehicleserviceschedule s
WHERE s.vehicleservice.vehicle.id = :vehicleId
""")
    LocalDate findLastScheduledDate(Integer vehicleId);
}
