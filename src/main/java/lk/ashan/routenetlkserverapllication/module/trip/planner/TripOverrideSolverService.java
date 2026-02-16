package lk.ashan.routenetlkserverapllication.module.trip.planner;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripOverrideSolverService {

    private final SolverFactory<TripSchedule> solverFactory;

    public Vehicle solveForTrip(
            Trip trip,
            List<Vehicle> candidateVehicles,
            List<Trip> existingTrips) {

        // Convert Vehicle → VehicleFact
        List<VehicleFact> vehicleFacts = candidateVehicles.stream()
                .map(v -> new VehicleFact(
                        v.getId(),
                        v.getNumber(),
                        v.getVehiclestatus().getName(),
                        v.getBranch().getId()
                ))
                .toList();


        // Create planning entity
        TripOverrideAssignment assignment = new TripOverrideAssignment(
                trip.getId(),
                trip,
                null  // No vehicle assigned yet
        );

        // Build problem
        TripSchedule problem = new TripSchedule(
                List.of(assignment),
                vehicleFacts,
                existingTrips,
                null  // No score yet
        );

        // Solve
        Solver<TripSchedule> solver = solverFactory.buildSolver();
        TripSchedule solved = solver.solve(problem);

        VehicleFact selected = solved.getTripAssignments()
                .get(0)
                .getAssignedVehicle();

        return candidateVehicles.stream()
                .filter(v -> v.getId().equals(selected.getId()))
                .findFirst()
                .orElse(null);
    }
}
