package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.*;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

public class TripExecutionConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory factory) {
        return new Constraint[] {
                vehicleOverlap(factory),
                driverOverlap(factory),
                conductorOverlap(factory),
                driverFamiliarity(factory),
                conductorFamiliarity(factory),
                requiredRestPeriod(factory),
                fairnessDriverDuty(factory),
                fairnessConductorDuty(factory)
        };
    }

    // 1. Vehicle Overlap
    Constraint vehicleOverlap(ConstraintFactory factory) {
        return factory.forEachUniquePair(TripExecutionPlanning.class,
                        Joiners.equal(TripExecutionPlanning::getVehicle),
                        Joiners.overlapping(TripExecutionPlanning::getDepartureTime, TripExecutionPlanning::getArrivalTime))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Vehicle overlap");
    }

    // 2. Driver Overlap
    private Constraint driverOverlap(ConstraintFactory factory) {
        return factory.forEachUniquePair(TripExecutionPlanning.class,
                        Joiners.equal(TripExecutionPlanning::getDriver),
                        Joiners.overlapping(TripExecutionPlanning::getDepartureTime, TripExecutionPlanning::getArrivalTime))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Driver overlap");
    }

    // 3. Conductor Overlap
    private Constraint conductorOverlap(ConstraintFactory factory) {
        return factory.forEachUniquePair(TripExecutionPlanning.class,
                        Joiners.equal(TripExecutionPlanning::getConductor),
                        Joiners.overlapping(TripExecutionPlanning::getDepartureTime, TripExecutionPlanning::getArrivalTime))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Conductor overlap");
    }

    // 4. Driver Familiarity (Fact-based logic)
    Constraint driverFamiliarity(ConstraintFactory factory) {
        return factory.forEach(TripExecutionPlanning.class)
                .filter(planning -> planning.getDriver().getFamiliarityLevel() <
                        planning.getRoute().getRequiredFamiliarityLevel())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Driver lacks familiarity");
    }

    // 5. Conductor Familiarity
    private Constraint conductorFamiliarity(ConstraintFactory factory) {
        return factory.forEach(TripExecutionPlanning.class)
                .filter(planning -> planning.getConductor().getFamiliarityLevel() <
                        planning.getRoute().getRequiredFamiliarityLevel())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Conductor lacks familiarity");
    }

    // 6. Rest Period (Using RouteFact or specific break logic)
    Constraint requiredRestPeriod(ConstraintFactory factory) {
        return factory.forEachUniquePair(TripExecutionPlanning.class,
                        Joiners.equal(TripExecutionPlanning::getDriver))
                .filter((t1, t2) -> {
                    TripExecutionPlanning first = t1.getArrivalTime().isBefore(t2.getDepartureTime()) ? t1 : t2;
                    TripExecutionPlanning second = (first == t1) ? t2 : t1;
                    long gap = Duration.between(first.getArrivalTime(), second.getDepartureTime()).toMinutes();

                    // Logic: If you don't have breakMinutes in RouteFact, you can hardcode or add it to RouteFact
                    return gap < 30; // Assuming 30 mins as a standard if not in Fact
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Insufficient rest");
    }

    Constraint fairnessDriverDuty(ConstraintFactory factory) {
        return factory.forEach(TripExecutionPlanning.class)
                .groupBy(
                        TripExecutionPlanning::getDriver,
                        ConstraintCollectors.sumLong(TripExecutionPlanning::getDurationMinutes)
                )
                .penalize(HardSoftScore.ONE_SOFT, (driver, newMinutes) -> {

                    long existingMinutes = driver.getTotalDutyMinutes() == null
                            ? 0
                            : driver.getTotalDutyMinutes();

                    long total = existingMinutes + newMinutes;

                    return (int) (total * total);
                })
                .asConstraint("Driver workload fairness");
    }


    private Constraint fairnessConductorDuty(ConstraintFactory factory) {
        return factory.forEach(TripExecutionPlanning.class)
                .groupBy(
                        TripExecutionPlanning::getConductor,
                        ConstraintCollectors.sumLong(TripExecutionPlanning::getDurationMinutes)
                )
                .penalize(HardSoftScore.ONE_SOFT, (conductor, newMinutes) -> {

                    long existingMinutes = conductor.getTotalDutyMinutes() == null
                            ? 0
                            : conductor.getTotalDutyMinutes();

                    long total = existingMinutes + newMinutes;

                    return (int) (total * total);
                })
                .asConstraint("Conductor workload fairness");
    }
}
