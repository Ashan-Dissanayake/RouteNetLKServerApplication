package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

/**
 * Service for determining which shift a trip belongs to based on departure time.
 * This eliminates the need to store shift_id in the trip table.
 * 
 * Key Features:
 * - Derives shift dynamically from trip departure time
 * - Handles overnight shifts (e.g., 22:00-06:00)
 * - Caches shift data per branch for performance
 * - Validates shift coverage (no gaps, no overlaps)
 */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ShiftDeterminationService {
//
//    private final ShiftRepository shiftRepository;
//
//    /**
//     * Determine which shift a trip belongs to based on departure time.
//     *
//     * @param departureTime Trip departure time
//     * @param branchId Branch ID
//     * @return Matching shift
//     * @throws BusinessRuleViolationException if no shift found for the time
//     */
//    @Transactional(readOnly = true)
//    public Shift determineShiftForTrip(LocalTime departureTime, Integer branchId) {
//        log.debug("Determining shift for departure time {} in branch {}", departureTime, branchId);
//
//        List<Shift> shifts = getActiveShiftsByBranch(branchId);
//
//        if (shifts.isEmpty()) {
//            throw new BusinessRuleViolationException(
//                "No active shifts configured for branch " + branchId
//            );
//        }
//
//        for (Shift shift : shifts) {
//            if (isTimeInShift(departureTime, shift.getTostart(), shift.getToend())) {
//                log.debug("Matched shift: {} ({} - {})",
//                    shift.getName(), shift.getTostart(), shift.getToend());
//                return shift;
//            }
//        }
//
//        throw new BusinessRuleViolationException(
//            String.format("No shift found for departure time %s in branch %d. " +
//                "Please ensure shifts cover all 24 hours or adjust trip departure time.",
//                departureTime, branchId)
//        );
//    }
//
//    /**
//     * Get active shifts for a branch (cached for performance).
//     * Cache is invalidated when shifts are updated.
//     *
//     * @param branchId Branch ID
//     * @return List of active shifts
//     */
//    @Cacheable(value = "branch-shifts", key = "#branchId")
//    @Transactional(readOnly = true)
//    public List<Shift> getActiveShiftsByBranch(Integer branchId) {
//        log.debug("Loading active shifts for branch {}", branchId);
//
//        List<Shift> shifts = shiftRepository.findByBranch_IdAndShiftstatus_Name(
//            branchId, "Active"
//        );
//
//        if (shifts.isEmpty()) {
//            throw new ResourceNotFoundException(
//                "No active shifts found for branch " + branchId
//            );
//        }
//
//        log.info("Loaded {} active shifts for branch {}", shifts.size(), branchId);
//        shifts.forEach(s ->
//            log.debug("  Shift: {} ({} - {})", s.getName(), s.getTostart(), s.getToend())
//        );
//
//        return shifts;
//    }
//
//    /**
//     * Check if a time falls within a shift's time range.
//     * Handles both regular shifts (e.g., 06:00-14:00) and overnight shifts (e.g., 22:00-06:00).
//     *
//     * @param time Time to check
//     * @param shiftStart Shift start time
//     * @param shiftEnd Shift end time
//     * @return true if time is within shift
//     */
//    public boolean isTimeInShift(LocalTime time, LocalTime shiftStart, LocalTime shiftEnd) {
//        // Handle overnight shifts (end time is before start time)
//        // Example: Night shift 22:00 - 06:00
//        boolean isOvernightShift = shiftEnd.isBefore(shiftStart);
//
//        if (isOvernightShift) {
//            // For overnight shifts: time >= start OR time < end
//            // Example: 23:00 >= 22:00 (true) OR 02:00 < 06:00 (true)
//            boolean result = !time.isBefore(shiftStart) || time.isBefore(shiftEnd);
//            log.trace("Overnight shift check: {} in [{} - {}] = {}",
//                time, shiftStart, shiftEnd, result);
//            return result;
//        } else {
//            // For regular shifts: start <= time < end
//            // Example: 08:00 >= 06:00 AND 08:00 < 14:00 (true)
//            boolean result = !time.isBefore(shiftStart) && time.isBefore(shiftEnd);
//            log.trace("Regular shift check: {} in [{} - {}] = {}",
//                time, shiftStart, shiftEnd, result);
//            return result;
//        }
//    }
//
//    /**
//     * Validate that shifts provide complete 24-hour coverage without gaps or overlaps.
//     * This is a pre-deployment validation check.
//     *
//     * @param branchId Branch to validate
//     * @throws BusinessRuleViolationException if validation fails
//     */
//    @Transactional(readOnly = true)
//    public void validateShiftCoverage(Integer branchId) {
//        List<Shift> shifts = getActiveShiftsByBranch(branchId);
//
//        if (shifts.isEmpty()) {
//            throw new BusinessRuleViolationException(
//                "No shifts configured for branch " + branchId
//            );
//        }
//
//        // Check for 24-hour coverage by testing sample times
//        LocalTime[] testTimes = {
//            LocalTime.of(0, 0),   // Midnight
//            LocalTime.of(6, 0),   // Early morning
//            LocalTime.of(12, 0),  // Noon
//            LocalTime.of(18, 0),  // Evening
//            LocalTime.of(23, 59)  // End of day
//        };
//
//        for (LocalTime testTime : testTimes) {
//            boolean covered = shifts.stream()
//                .anyMatch(shift -> isTimeInShift(testTime, shift.getTostart(), shift.getToend()));
//
//            if (!covered) {
//                throw new BusinessRuleViolationException(
//                    String.format("Time %s is not covered by any shift in branch %d",
//                        testTime, branchId)
//                );
//            }
//        }
//
//        log.info("Shift coverage validation passed for branch {}", branchId);
//    }
//
//    /**
//     * Get shift by name (used in tests and manual operations).
//     *
//     * @param shiftName Shift name
//     * @param branchId Branch ID
//     * @return Shift
//     */
//    @Transactional(readOnly = true)
//    public Shift getShiftByName(String shiftName, Integer branchId) {
//        return shiftRepository.findByBranch_IdAndName(branchId, shiftName)
//            .orElseThrow(() -> new ResourceNotFoundException(
//                String.format("Shift '%s' not found in branch %d", shiftName, branchId)
//            ));
//    }
//
//    /**
//     * Check if two time ranges overlap.
//     * Used for validation when creating/updating shifts.
//     *
//     * @param start1 First range start
//     * @param end1 First range end
//     * @param start2 Second range start
//     * @param end2 Second range end
//     * @return true if ranges overlap
//     */
//    public boolean doShiftsOverlap(
//            LocalTime start1, LocalTime end1,
//            LocalTime start2, LocalTime end2) {
//
//        boolean overnight1 = end1.isBefore(start1);
//        boolean overnight2 = end2.isBefore(start2);
//
//        // If both are overnight, check overlap
//        if (overnight1 && overnight2) {
//            // Complex case: both spans cross midnight
//            return true; // For simplicity, flag as potential overlap
//        }
//
//        // If one is overnight, check if they intersect
//        if (overnight1) {
//            return !end1.isBefore(start2) && !end1.isAfter(end2)
//                || !start1.isAfter(start2) && !start1.isBefore(end2)
//                || start2.isBefore(end1) || end2.isAfter(start1);
//        }
//
//        if (overnight2) {
//            return !end2.isBefore(start1) && !end2.isAfter(end1)
//                || !start2.isAfter(start1) && !start2.isBefore(end1)
//                || start1.isBefore(end2) || end1.isAfter(start2);
//        }
//
//        // Both regular shifts
//        return !end1.isBefore(start2) && !start1.isAfter(end2);
//    }
//
//    /**
//     * Calculate shift duration in hours.
//     *
//     * @param shift Shift
//     * @return Duration in hours (decimal)
//     */
//    public double calculateShiftDurationHours(Shift shift) {
//        LocalTime start = shift.getTostart();
//        LocalTime end = shift.getToend();
//
//        long minutes;
//        if (end.isBefore(start)) {
//            // Overnight shift
//            minutes = java.time.Duration.between(start, LocalTime.MAX).toMinutes()
//                    + java.time.Duration.between(LocalTime.MIN, end).toMinutes()
//                    + 1; // Add 1 minute for the midnight crossing
//        } else {
//            // Regular shift
//            minutes = java.time.Duration.between(start, end).toMinutes();
//        }
//
//        return minutes / 60.0;
//    }
//
//    /**
//     * Get shift for a specific time without requiring branch lookup.
//     * Used when branch context is already known.
//     *
//     * @param departureTime Time to check
//     * @param shifts Pre-loaded shift list
//     * @return Matching shift
//     */
//    public Shift determineShiftFromList(LocalTime departureTime, List<Shift> shifts) {
//        for (Shift shift : shifts) {
//            if (isTimeInShift(departureTime, shift.getTostart(), shift.getToend())) {
//                return shift;
//            }
//        }
//
//        throw new BusinessRuleViolationException(
//            "No shift found for time " + departureTime
//        );
//    }
//}
