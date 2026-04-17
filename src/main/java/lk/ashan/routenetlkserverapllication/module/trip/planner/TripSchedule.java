//package lk.ashan.routenetlkserverapllication.module.trip.planner;
//
//import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
//import org.optaplanner.core.api.domain.solution.PlanningScore;
//import org.optaplanner.core.api.domain.solution.PlanningSolution;
//import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
//import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
//import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
//
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@PlanningSolution
//public class TripSchedule {
//
//    @PlanningEntityCollectionProperty
//    private List<TripOverrideAssignment> tripAssignments;
//
//    @ProblemFactCollectionProperty
//    @ValueRangeProvider(id = "vehicleRange")
//    private List<VehicleFact> vehicleList;
//
//    @ProblemFactCollectionProperty
//    private List<Trip> existingTrips;
//
//    @PlanningScore
//    private HardSoftScore score;
//}
