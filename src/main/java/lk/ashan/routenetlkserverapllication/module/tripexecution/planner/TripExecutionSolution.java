package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lombok.*;

import java.util.List;

@PlanningSolution
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TripExecutionSolution {

    @PlanningId
    @NonNull
    private Integer id;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "vehicleRange")
    @NonNull
    private List<VehicleFact> vehicleList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "driverRange")
    @NonNull
    private List<CrewFact> driverList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "conductorRange")
    @NonNull
    private List<CrewFact> conductorList;

    @PlanningEntityCollectionProperty
    @NonNull
    private List<TripExecutionPlanning> tripExecutionList;

    @PlanningScore
    private HardSoftScore score;
}
