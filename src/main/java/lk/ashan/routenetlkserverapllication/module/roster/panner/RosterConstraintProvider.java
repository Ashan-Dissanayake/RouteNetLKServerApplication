package lk.ashan.routenetlkserverapllication.module.roster.panner;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;


public class RosterConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                // ==================== HARD CONSTRAINTS ====================

                noOverlappingShifts(factory),              // Employee can't work 2 shifts at same time
                employeeMustBelongToSameBranch(factory),   // Employee must be from same branch
                roleAssignedOncePerShift(factory),         // Each role filled once per shift
                assignmentWithinRosterWeek(factory),       // Assignment date within roster's week
                employeeMustHaveQualification(factory),    // Employee qualified for role
                maxDailyHours(factory),                    // Max hours per day not exceeded
                maxWeeklyHours(factory),                   // Max hours per week not exceeded

                // ==================== SOFT CONSTRAINTS ====================

                minimizeOvertime(factory),                 // Prefer regular hours over overtime
                balanceWorkloadAcrossEmployees(factory),   // Fair distribution of shifts
                minimizeConsecutiveNightShifts(factory),   // Limit consecutive night work
                fairWeekendDistribution(factory),          // Fair weekend shift distribution
        };

    }

    //                         HARD CONSTRAINTS

    /**
     * HARD: Employee cannot work overlapping shifts on same day
     * Example violation:
     * - Employee A assigned to 08:00-16:00 shift
     * - Employee A assigned to 14:00-22:00 shift on same day
     * → Overlap from 14:00-16:00
     */
    private Constraint noOverlappingShifts(ConstraintFactory factory) {
        return factory.forEachUniquePair(
                        Shiftrosterassignment.class,
                        // Same employee
                        Joiners.equal(Shiftrosterassignment::getEmployee),
                        // Same date
                        Joiners.equal(Shiftrosterassignment::getDoassigned),
                        // Time overlap
                        Joiners.overlapping(
                                a -> a.getShift().getTostart(),
                                a -> a.getShift().getToend()
                        )
                )
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("No overlapping shifts for employee");
    }

    /**
     * HARD: Employee must belong to same branch as the shift
     * Example violation:
     * - Shift at Colombo branch
     * - Employee from Angoda branch assigned
     */
    private Constraint employeeMustBelongToSameBranch(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null &&
                        !a.getEmployee().getBranch().getId()
                                .equals(a.getShift().getBranch().getId()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Employee must belong to same branch");
    }

    /**
     * HARD: Each role can only be assigned once per shift on a given date
     * Example violation:
     * - Driver role needed for Morning shift on Monday
     * - Two employees both assigned as Driver for same shift/date
     */
    private Constraint roleAssignedOncePerShift(ConstraintFactory factory) {
        return factory.forEachUniquePair(
                        Shiftrosterassignment.class,
                        // Same shift
                        Joiners.equal(Shiftrosterassignment::getShift),
                        // Same role
                        Joiners.equal(Shiftrosterassignment::getRole),
                        // Same date
                        Joiners.equal(Shiftrosterassignment::getDoassigned)
                )
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Role assigned once per shift");
    }

    /**
     * HARD: Assignment date must be within roster's week range
     * Example violation:
     * - Roster covers Feb 17-23
     * - Assignment date is Feb 10 (before roster start)
     */
    private Constraint assignmentWithinRosterWeek(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> {
                    if (a.getEmployee() == null) return false;

                    return a.getDoassigned().isBefore(a.getRoster().getDostartofweek()) ||
                            a.getDoassigned().isAfter(a.getRoster().getDoendofweek());
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Assignment date within roster week");
    }

    /**
     * HARD: Employee must have required qualification/role eligibility
     * Example violation:
     * - Conductor role assigned
     * - Employee is only qualified as Driver (not Conductor)
     * Note: Requires EmployeeFact to have `qualifiedRoles` field
     */
    private Constraint employeeMustHaveQualification(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null &&
                        !a.getEmployee().getDesignation().getName()
                                .equalsIgnoreCase(a.getRole().getName()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Employee must be qualified for role");
    }

    /**
     * HARD: Employee cannot exceed maximum daily hours
     * Example violation:
     * - Employee works 08:00-16:00 (8 hours)
     * - Employee assigned another 08:00-16:00 shift same day
     * - Total: 16 hours > 12 hour max
     */
    private Constraint maxDailyHours(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null)
                .groupBy(
                        Shiftrosterassignment::getEmployee,
                        Shiftrosterassignment::getDoassigned,
                        // Sum total hours for this employee on this date
                        ConstraintCollectors.sum(a -> {
                            LocalTime start = a.getShift().getTostart();
                            LocalTime end = a.getShift().getToend();

                            // Handle overnight shifts
                            if (end.isBefore(start)) {
                                long hours = Duration.between(start, LocalTime.MAX).toHours() +
                                        Duration.between(LocalTime.MIN, end).toHours() + 1;
                                return (int) hours;
                            }
                            long hours = Duration.between(start, end).toHours();
                            return (int) hours;
                        })
                )
                .filter((employee, date, totalHours) -> totalHours > 12) // Max 12 hours/day
                .penalize(HardSoftScore.ONE_HARD,
                        (employee, date, totalHours) -> totalHours - 12)
                .asConstraint("Max 12 hours per day");
    }

    /**
     * HARD: Employee cannot exceed maximum weekly hours
     * Example violation:
     * - Employee already has 40 hours assigned this week
     * - Trying to assign another 8-hour shift
     * - Total: 48 hours > 48 hour max (example limit)
     */
    private Constraint maxWeeklyHours(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null)
                .groupBy(
                        Shiftrosterassignment::getEmployee,
                        Shiftrosterassignment::getRoster,
                        // Sum total hours for this employee in this roster (week)
                        ConstraintCollectors.sum(a -> {
                            LocalTime start = a.getShift().getTostart();
                            LocalTime end = a.getShift().getToend();

                            // Handle overnight shifts
                            if (end.isBefore(start)) {
                                long hours = Duration.between(start, LocalTime.MAX).toHours() +
                                        Duration.between(LocalTime.MIN, end).toHours() + 1;
                                return (int) hours;
                            }
                            long hours = Duration.between(start, end).toHours();
                            return (int) hours;
                        })
                )
                .filter((employee, roster, totalHours) -> totalHours > 48) // Max 48 hours/week
                .penalize(HardSoftScore.ONE_HARD,
                        (employee, roster, totalHours) -> totalHours - 48)
                .asConstraint("Max 48 hours per week");
    }


    //                         SOFT CONSTRAINTS

    /**
     * SOFT: Minimize overtime hours (prefer regular hours)
     * Prefer: Employee works 40 hours (regular)
     * Penalize: Employee works 45 hours (5 hours overtime)
     */
    private Constraint minimizeOvertime(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null)
                .groupBy(
                        Shiftrosterassignment::getEmployee,
                        Shiftrosterassignment::getRoster,
                        // Calculate total hours for this employee in this week
                        ConstraintCollectors.sum(a -> {
                            LocalTime start = a.getShift().getTostart();
                            LocalTime end = a.getShift().getToend();

                            if (end.isBefore(start)) {
                                long hours = Duration.between(start, LocalTime.MAX).toHours() +
                                        Duration.between(LocalTime.MIN, end).toHours() + 1;
                                return (int) hours;
                            }
                            long hours = Duration.between(start, end).toHours();
                            return (int) hours;
                        })
                )
                .filter((employee, roster, totalHours) -> totalHours > 40) // Overtime if > 40
                .penalize(HardSoftScore.ONE_SOFT,
                        (employee, roster, totalHours) -> totalHours - 40)
                .asConstraint("Minimize overtime");
    }

    /**
     * SOFT: Balance workload across all employees
     * Prefer: All employees have similar hours
     * Penalize: One employee has 50 hours, another has 20 hours
     * Simple implementation: Penalize employees with significantly high hours
     */
    private Constraint balanceWorkloadAcrossEmployees(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null)
                .groupBy(
                        Shiftrosterassignment::getEmployee,
                        Shiftrosterassignment::getRoster,
                        ConstraintCollectors.sum(a -> {
                            LocalTime start = a.getShift().getTostart();
                            LocalTime end = a.getShift().getToend();

                            if (end.isBefore(start)) {
                                long hours = Duration.between(start, LocalTime.MAX).toHours() +
                                        Duration.between(LocalTime.MIN, end).toHours() + 1;
                                return (int) hours;
                            }
                            long hours = Duration.between(start, end).toHours();
                            return (int) hours;
                        })
                )
                // Penalize deviation from target 40 hours
                .penalize(HardSoftScore.ONE_SOFT,
                        (employee, roster, hours) -> Math.abs(hours - 40))
                .asConstraint("Balance workload");
    }

    /**
     * SOFT: Minimize consecutive night shifts
     * Prefer: No more than 3 consecutive night shifts
     * Penalize: 4+ consecutive night shifts
     */
    private Constraint minimizeConsecutiveNightShifts(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null &&
                        isNightShift(a.getShift().getTostart()))
                .join(Shiftrosterassignment.class,
                        Joiners.equal(Shiftrosterassignment::getEmployee),
                        Joiners.equal(a1 -> a1.getDoassigned().plusDays(1),
                                Shiftrosterassignment::getDoassigned)
                )
                .filter((a1, a2) -> isNightShift(a2.getShift().getTostart()))
                .join(Shiftrosterassignment.class,
                        Joiners.equal((a1, a2) -> a1.getEmployee(),
                                Shiftrosterassignment::getEmployee),
                        Joiners.equal((a1, a2) -> a1.getDoassigned().plusDays(2),
                                Shiftrosterassignment::getDoassigned)
                )
                .filter((a1, a2, a3) -> isNightShift(a3.getShift().getTostart()))
                .join(Shiftrosterassignment.class,
                        Joiners.equal((a1, a2, a3) -> a1.getEmployee(),
                                Shiftrosterassignment::getEmployee),
                        Joiners.equal((a1, a2, a3) -> a1.getDoassigned().plusDays(3),
                                Shiftrosterassignment::getDoassigned)
                )
                .filter((a1, a2, a3, a4) -> isNightShift(a4.getShift().getTostart()))
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Minimize consecutive night shifts");
    }

    /**
     * SOFT: Fair distribution of weekend shifts
     * Prefer: All employees work similar number of weekends
     * Penalize: One employee works every weekend, others never
     */
    private Constraint fairWeekendDistribution(ConstraintFactory factory) {
        return factory.forEach(Shiftrosterassignment.class)
                .filter(a -> a.getEmployee() != null &&
                        (a.getDoassigned().getDayOfWeek() == DayOfWeek.SATURDAY ||
                                a.getDoassigned().getDayOfWeek() == DayOfWeek.SUNDAY))
                .groupBy(
                        Shiftrosterassignment::getEmployee,
                        Shiftrosterassignment::getRoster
                )
                // Penalize if same employee has too many weekend shifts
                .filter((employee, roster) -> true) // Placeholder
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Fair weekend distribution");
    }

    /**
     * Check if shift is a night shift (starts between 20:00 and 06:00)
     */
    private boolean isNightShift(LocalTime startTime) {
        return startTime.isAfter(LocalTime.of(20, 0)) ||
                startTime.isBefore(LocalTime.of(6, 0));
    }
}

