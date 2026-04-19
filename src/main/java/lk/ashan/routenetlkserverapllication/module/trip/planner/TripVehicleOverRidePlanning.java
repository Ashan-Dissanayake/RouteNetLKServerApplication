package lk.ashan.routenetlkserverapllication.module.trip.planner;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningEntity
public class TripVehicleOverRidePlanning {

    @PlanningId
    private Integer id;

    private Trip trip;

    @PlanningVariable(valueRangeProviderRefs = "vehicleRange")
    private VehicleFact assignedVehicle;
}
