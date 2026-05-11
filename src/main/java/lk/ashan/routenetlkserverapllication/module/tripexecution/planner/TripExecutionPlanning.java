package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@PlanningEntity
@Data
@NoArgsConstructor
public class TripExecutionPlanning {

    @PlanningId
    private Integer id;

    private RouteFact route;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    @PlanningVariable(valueRangeProviderRefs = "vehicleRange")
    private VehicleFact vehicle;

    @PlanningVariable(valueRangeProviderRefs = "driverRange")
    private CrewFact driver;

    @PlanningVariable(valueRangeProviderRefs = "conductorRange")
    private CrewFact conductor;

    public long getDurationMinutes() {
        return Duration.between(departureTime, arrivalTime).toMinutes();
    }
}
