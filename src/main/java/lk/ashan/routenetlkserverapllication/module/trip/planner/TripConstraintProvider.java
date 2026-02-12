package lk.ashan.routenetlkserverapllication.module.trip.planner;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class TripConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                vehicleNotInUseAtSameTime(factory),
                vehicleMustBeAvailable(factory),
                vehicleDepotMatch(factory),
                noConflictWithExistingTrips(factory)
        };
    }

    // 1️⃣ No two override assignments use same vehicle at overlapping time
    private Constraint vehicleNotInUseAtSameTime(ConstraintFactory factory) {
        return factory.forEachUniquePair(TripOverrideAssignment.class,
                        Joiners.equal(TripOverrideAssignment::getAssignedVehicle),
                        Joiners.overlapping(
                                a -> a.getTrip().getTodepature(),
                                a -> a.getTrip().getToarrival()
                        ))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Vehicle conflict within override assignments");
    }

    // 2️⃣ Vehicle must be AVAILABLE
    private Constraint vehicleMustBeAvailable(ConstraintFactory factory) {
        return factory.forEach(TripOverrideAssignment.class)
                .filter(a -> a.getAssignedVehicle() != null &&
                        !"AVAILABLE".equals(a.getAssignedVehicle().getStatus()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Vehicle not available");
    }

    // 3️⃣ Depot must match
    private Constraint vehicleDepotMatch(ConstraintFactory factory) {
        return factory.forEach(TripOverrideAssignment.class)
                .filter(a -> a.getAssignedVehicle() != null &&
                        !a.getAssignedVehicle().getDepotId()
                                .equals(a.getTrip().getBranch().getId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Vehicle must belong to same depot");
    }

    // 4️⃣ Prevent conflict with already scheduled trips
    private Constraint noConflictWithExistingTrips(ConstraintFactory factory) {
        return factory.forEach(TripOverrideAssignment.class)
                .join(Trip.class,
                        Joiners.equal(
                                a -> a.getAssignedVehicle() != null ? a.getAssignedVehicle().getId() : null,
                                t -> t.getPermite().getVehicle() != null ? t.getPermite().getVehicle().getId() : null
                        ),
                        Joiners.overlapping(
                                a -> a.getTrip().getTodepature(),
                                a -> a.getTrip().getToarrival(),
                                t -> t.getTodepature(),
                                t -> t.getToarrival()
                        ))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Vehicle conflicts with existing trip");
    }
}
