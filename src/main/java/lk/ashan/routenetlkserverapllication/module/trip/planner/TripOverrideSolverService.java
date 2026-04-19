package lk.ashan.routenetlkserverapllication.module.trip.planner;

import ai.timefold.solver.core.api.solver.SolverManager;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class TripOverrideSolverService {

    @Qualifier("tripSolver")
    private final SolverManager<TripVehicleOverRideSolution, Integer> solverManager;

    public Vehicle solveForTrip(
            Trip trip,
            List<Vehicle> candidateVehicles,
            List<Trip> existingTrips) {

        List<VehicleFact> vehicleFacts = candidateVehicles.stream()
                .map(v -> new VehicleFact(
                        v.getId(),
                        v.getNumber(),
                        v.getVehiclestatus().getName(),
                        v.getBranch().getId()
                ))
                .toList();

        TripVehicleOverRidePlanning assignment = new TripVehicleOverRidePlanning(
                trip.getId(),
                trip,
                null
        );

        TripVehicleOverRideSolution problem = new TripVehicleOverRideSolution(
                List.of(assignment),
                vehicleFacts,
                existingTrips,
                null
        );


        TripVehicleOverRideSolution solved;
        try {
            solved = solverManager.solve(trip.getId(), problem).getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Solving failed for trip: " + trip.getId(), e);
        }

        VehicleFact selected = solved.getTripAssignments()
                .get(0)
                .getAssignedVehicle();

        if (selected == null) return null;

        return candidateVehicles.stream()
                .filter(v -> v.getId().equals(selected.getId()))
                .findFirst()
                .orElse(null);
    }
}
