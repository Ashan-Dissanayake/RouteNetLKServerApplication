package lk.ashan.routenetlkserverapllication.module.trip.planner;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningSolution
public class TripVehicleOverRideSolution {

    @PlanningEntityCollectionProperty
    private List<TripVehicleOverRidePlanning> tripAssignments;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "vehicleRange")
    private List<VehicleFact> vehicleList;

    @ProblemFactCollectionProperty
    private List<Trip> existingTrips;

    @PlanningScore
    private HardSoftScore score;
}
