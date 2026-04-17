package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.planner;



import java.time.Duration;
import java.time.LocalTime;

/**
 * Constraint Provider for Trip Crew Allocation.
 *
 * Defines all hard and soft constraints that guide OptaPlanner's optimization.
 *
 * HARD CONSTRAINTS (Must Not Violate):
 * 1. No double assignment - employee can't work overlapping trips
 * 2. Driver license category must match vehicle type
 * 3. Conductor route familiarity must meet minimum
 * 4. Roster validity - only CONFIRMED roster employees
 * 5. Branch alignment - employee.branch = trip.branch
 * 6. Shift timing alignment - trip within shift hours
 * 7. Role qualification - employee must be qualified for role
 *
 * SOFT CONSTRAINTS (Optimize When Possible):
 * 1. Fair workload distribution - balance trips per employee
 * 2. Route familiarity optimization - prefer higher familiarity
 * 3. Rest period between trips - minimum 30 minutes
 * 4. Trip-role preference - assign to primary role
 */
//public class TripCrewConstraintProvider implements ConstraintProvider {

//    @Override
//    public Constraint[] defineConstraints(ConstraintFactory factory) {
//        return new Constraint[]{
//                // ========== HARD CONSTRAINTS ==========
//                noDoubleAssignment(factory),
//                driverLicenseCategoryMatch(factory),
//                conductorRouteFamiliarity(factory),
//                branchAlignment(factory),
//                shiftTimingAlignment(factory),
//                roleQualification(factory),
//
//                // ========== SOFT CONSTRAINTS ==========
//                fairWorkloadDistribution(factory),
//                routeFamiliarityOptimization(factory),
//                restPeriodBetweenTrips(factory),
//                tripRolePreference(factory)
//        };
//    }
//
//    // ==================== HARD CONSTRAINTS ====================
//
//    /**
//     * HARD: No employee can work two overlapping trips on same date.
//     *
//     * Example violation:
//     * - Trip A: 08:00-10:00, assigned Employee 1
//     * - Trip B: 09:00-11:00, assigned Employee 1 (CONFLICT!)
//     */
//    private Constraint noDoubleAssignment(ConstraintFactory factory) {
//        return factory.forEachUniquePair(
//                        TripCrewAssignmentPlanning.class,
//                        // Same employee
//                        Joiners.equal(TripCrewAssignmentPlanning::getAssignedEmployeeId),
//                        // Same date
//                        Joiners.equal(a -> a.getTrip().getDoservice()),
//                        // Overlapping time
//                        Joiners.overlapping(
//                                a -> a.getTrip().getTodepature(),
//                                a -> a.getTrip().getToarrival()
//                        ))
//                .filter((a1, a2) ->
//                        a1.getAssignedEmployee() != null &&
//                                a2.getAssignedEmployee() != null)
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("No overlapping trip assignments for same employee");
//    }
//
//    /**
//     * HARD: Driver's license category must be compatible with vehicle bus type.
//     *
//     * Mapping (example):
//     * - Bus Type A+/A/AA requires License Category C1 or E
//     * - Bus Type B/C requires License Category B or C1
//     *
//     * This is simplified - adjust based on your actual business rules.
//     */
//    private Constraint driverLicenseCategoryMatch(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a ->
//                        a.getAssignedEmployee() != null &&
//                                "Driver".equals(a.getRoleName()) &&
//                                !isLicenseCategoryCompatible(
//                                        a.getAssignedEmployee().getLicenseCategoryId(),
//                                        a.getTripVehicleBusTypeId()
//                                ))
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Driver license category must match vehicle type");
//    }
//
//    /**
//     * HARD: Conductor must have minimum route familiarity for the route.
//     *
//     * Levels (example):
//     * - Interprovincial routes require High (3) or Medium (2)
//     * - Intra provincial routes require Medium (2) or Low (1)
//     *
//     * This is simplified - adjust based on your route types.
//     */
//    private Constraint conductorRouteFamiliarity(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a ->
//                        a.getAssignedEmployee() != null &&
//                                "Conductor".equals(a.getRoleName()) &&
//                                !hasMinimumRouteFamiliarity(
//                                        a.getAssignedEmployee().getRouteFamiliarityLevelId(),
//                                        a.getTripRouteId()
//                                ))
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Conductor must have required route familiarity");
//    }
//
//    /**
//     * HARD: Employee must belong to same branch as trip.
//     */
//    private Constraint branchAlignment(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a ->
//                        a.getAssignedEmployee() != null &&
//                                !a.getAssignedEmployee().getBranchId().equals(a.getTripBranchId()))
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Employee must belong to same branch as trip");
//    }
//
//    /**
//     * HARD: Trip timing must fall within shift hours.
//     *
//     * Validates that:
//     * - Departure time >= shift start
//     * - Arrival time <= shift end
//     * - Handles overnight shifts correctly
//     */
//    private Constraint shiftTimingAlignment(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a -> {
//                    if (a.getAssignedEmployee() == null) return false;
//
//                    LocalTime tripStart = a.getTrip().getTodepature();
//                    LocalTime tripEnd = a.getTrip().getToarrival();
//                    LocalTime shiftStart = a.getDerivedShift().getTostart();
//                    LocalTime shiftEnd = a.getDerivedShift().getToend();
//
//                    return !isTripWithinShift(tripStart, tripEnd, shiftStart, shiftEnd);
//                })
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Trip must fall within shift timing");
//    }
//
//    /**
//     * HARD: Employee must be qualified for the role.
//     *
//     * Driver role (1) requires driver qualification.
//     * Conductor role (2) requires conductor qualification.
//     */
//    private Constraint roleQualification(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a ->
//                        a.getAssignedEmployee() != null &&
//                                !a.getAssignedEmployee().getQualifiedRoles().contains(a.getRoleId()))
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Employee must be qualified for the role");
//    }
//
//    // ==================== SOFT CONSTRAINTS ====================
//
//    /**
//     * SOFT: Balance workload - penalize assigning too many trips to one employee.
//     *
//     * Uses quadratic penalty: more trips = exponentially worse score.
//     * This encourages fair distribution.
//     */
//    private Constraint fairWorkloadDistribution(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a -> a.getAssignedEmployee() != null)
//                .groupBy(
//                        TripCrewAssignmentPlanning::getAssignedEmployeeId,
//                        ConstraintCollectors.count()
//                )
//                .penalize(HardSoftScore.ONE_SOFT,
//                        (employeeId, count) -> count * count) // Quadratic penalty
//                .asConstraint("Balance workload across employees");
//    }
//
//    /**
//     * SOFT: Prefer higher route familiarity conductors for routes.
//     *
//     * Rewards assigning High familiarity (score +2) over Medium (+1) over Low (0).
//     */
//    private Constraint routeFamiliarityOptimization(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a ->
//                        a.getAssignedEmployee() != null &&
//                                "Conductor".equals(a.getRoleName()))
//                .reward(HardSoftScore.ONE_SOFT,
//                        a -> {
//                            Integer familiarityId = a.getAssignedEmployee().getRouteFamiliarityLevelId();
//                            if (familiarityId == null) return 0;
//                            return switch (familiarityId) {
//                                case 3 -> 2;  // High familiarity
//                                case 2 -> 1;  // Medium familiarity
//                                default -> 0; // Low familiarity
//                            };
//                        })
//                .asConstraint("Prefer higher route familiarity");
//    }
//
//    /**
//     * SOFT: Ensure minimum rest period (30 min) between consecutive trips.
//     *
//     * Penalizes assigning employee to trips with insufficient rest.
//     */
//    private Constraint restPeriodBetweenTrips(ConstraintFactory factory) {
//        return factory.forEachUniquePair(
//                        TripCrewAssignmentPlanning.class,
//                        // Same employee
//                        Joiners.equal(TripCrewAssignmentPlanning::getAssignedEmployeeId),
//                        // Same date
//                        Joiners.equal(a -> a.getTrip().getDoservice()))
//                .filter((a1, a2) -> {
//                    if (a1.getAssignedEmployee() == null || a2.getAssignedEmployee() == null) {
//                        return false;
//                    }
//
//                    // Get end of first trip and start of second
//                    LocalTime end1 = a1.getTrip().getToarrival();
//                    LocalTime start2 = a2.getTrip().getTodepature();
//
//                    // Calculate gap
//                    long minutes = Math.abs(Duration.between(end1, start2).toMinutes());
//
//                    // Penalize if less than 30 minutes
//                    return minutes < 30;
//                })
//                .penalize(HardSoftScore.ONE_SOFT)
//                .asConstraint("Minimum 30 minute rest between trips");
//    }
//
//    /**
//     * SOFT: Prefer assigning employees to their primary role.
//     *
//     * If employee is qualified for both driver and conductor,
//     * prefer assigning to the role matching their primary qualification.
//     */
//    private Constraint tripRolePreference(ConstraintFactory factory) {
//        return factory.forEach(TripCrewAssignmentPlanning.class)
//                .filter(a -> a.getAssignedEmployee() != null)
//                .reward(HardSoftScore.ONE_SOFT,
//                        a -> {
//                            // If employee is qualified for assigned role, reward
//                            if (a.getAssignedEmployee().getQualifiedRoles().contains(a.getRoleId())) {
//                                return 1;
//                            }
//                            return 0;
//                        })
//                .asConstraint("Prefer employees in their qualified roles");
//    }
//
//    // ==================== HELPER METHODS ====================
//
//    /**
//     * Check if license category is compatible with bus type.
//     *
//     * Business rules (adjust as needed):
//     * - Category E (3): All bus types
//     * - Category C1 (2): Bus types A+, A, AA, B, B+
//     * - Category B (1): Bus types B, B+, C, D, E
//     */
//    private boolean isLicenseCategoryCompatible(Integer licenseCategoryId, Integer busTypeId) {
//        if (licenseCategoryId == null || busTypeId == null) return false;
//
//        // Category E can drive anything
//        if (licenseCategoryId == 3) return true;
//
//        // Category C1 for larger buses
//        if (licenseCategoryId == 2 && busTypeId <= 5) return true;
//
//        // Category B for smaller buses
//        if (licenseCategoryId == 1 && busTypeId >= 4) return true;
//
//        return false;
//    }
//
//    /**
//     * Check if conductor has minimum familiarity for route.
//     *
//     * Business rules (simplified):
//     * - High familiarity (3): Can handle any route
//     * - Medium familiarity (2): Can handle most routes
//     * - Low familiarity (1): Only simple local routes
//     */
//    private boolean hasMinimumRouteFamiliarity(Integer familiarityId, Integer routeId) {
//        if (familiarityId == null || routeId == null) return false;
//
//        // High familiarity OK for all routes
//        if (familiarityId == 3) return true;
//
//        // Medium familiarity OK for most routes (adjust as needed)
//        if (familiarityId == 2) return true;
//
//        // Low familiarity only for simple routes (route IDs > 100 in your system?)
//        if (familiarityId == 1 && routeId > 100) return true;
//
//        return false;
//    }
//
//    /**
//     * Check if trip timing falls within shift.
//     * Handles overnight shifts correctly.
//     */
//    private boolean isTripWithinShift(
//            LocalTime tripStart, LocalTime tripEnd,
//            LocalTime shiftStart, LocalTime shiftEnd) {
//
//        boolean isOvernightShift = shiftEnd.isBefore(shiftStart);
//
//        if (isOvernightShift) {
//            // Overnight shift: trip start must be >= shift start OR < shift end
//            boolean startOK = !tripStart.isBefore(shiftStart) || tripStart.isBefore(shiftEnd);
//            // Trip end must be <= shift end OR > shift start
//            boolean endOK = !tripEnd.isAfter(shiftEnd) || tripEnd.isAfter(shiftStart);
//            return startOK && endOK;
//        } else {
//            // Regular shift: start >= shift start AND end <= shift end
//            return !tripStart.isBefore(shiftStart) && !tripEnd.isAfter(shiftEnd);
//        }
//    }
//}
