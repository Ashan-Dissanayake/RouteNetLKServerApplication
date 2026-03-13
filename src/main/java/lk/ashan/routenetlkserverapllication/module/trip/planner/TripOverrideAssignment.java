package lk.ashan.routenetlkserverapllication.module.trip.planner;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningEntity
public class TripOverrideAssignment {

    @PlanningId
    private Integer id;

    private Trip trip; // problem fact reference

    @PlanningVariable(valueRangeProviderRefs = "vehicleRange")
    private VehicleFact assignedVehicle;
}
