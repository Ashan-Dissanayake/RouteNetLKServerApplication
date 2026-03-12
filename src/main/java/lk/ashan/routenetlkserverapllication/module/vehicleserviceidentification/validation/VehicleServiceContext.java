package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class VehicleServiceContext {
    private Vehicle vehicle;
    private Incident incident;
    private Integer odometer;
    private boolean preTripRequired;
    private Integer mileage;
    private Integer lastServiceMileage;

}
