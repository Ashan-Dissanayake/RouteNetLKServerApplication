package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
