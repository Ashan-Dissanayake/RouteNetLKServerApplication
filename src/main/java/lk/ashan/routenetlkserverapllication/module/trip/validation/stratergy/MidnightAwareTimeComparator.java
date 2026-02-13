package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Utility for handling time comparisons that may cross midnight boundary
 * Addresses UAT 12: Midnight boundary trip handling
 */
public class MidnightAwareTimeComparator {
    
    /**
     * Calculates the duration between two times, accounting for midnight crossing
     * 
     * @param startTime Start time
     * @param endTime End time
     * @return Duration in minutes (positive if endTime is after startTime)
     */
    public static long calculateDurationMinutes(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return 0;
        }
        
        // If end time is before start time, assume it crosses midnight
        if (endTime.isBefore(startTime)) {
            // Calculate time until midnight + time after midnight
            Duration untilMidnight = Duration.between(startTime, LocalTime.MAX);
            Duration afterMidnight = Duration.between(LocalTime.MIN, endTime);
            return untilMidnight.toMinutes() + afterMidnight.toMinutes() + 1; // +1 for the midnight second
        }
        
        // Normal case - same day
        return Duration.between(startTime, endTime).toMinutes();
    }
    
    /**
     * Checks if two time windows overlap, accounting for midnight crossing
     * 
     * @param start1 Start of first window
     * @param end1 End of first window
     * @param start2 Start of second window
     * @param end2 End of second window
     * @return true if windows overlap
     */
    public static boolean hasOverlap(
            LocalTime start1, 
            LocalTime end1, 
            LocalTime start2, 
            LocalTime end2) {
        
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        
        // Check if either window crosses midnight
        boolean window1CrossesMidnight = end1.isBefore(start1);
        boolean window2CrossesMidnight = end2.isBefore(start2);
        
        if (!window1CrossesMidnight && !window2CrossesMidnight) {
            // Standard overlap check - both windows are within same day
            return start1.isBefore(end2) && start2.isBefore(end1);
        }
        
        if (window1CrossesMidnight && !window2CrossesMidnight) {
            // Window 1 crosses midnight, window 2 doesn't
            // Window 1 occupies: [start1, 23:59:59] and [00:00:00, end1]
            // Overlap if window 2 intersects either segment
            return start2.isBefore(LocalTime.MAX) && (start2.isAfter(start1) || start2.equals(start1)) ||
                   end2.isAfter(LocalTime.MIN) && (end2.isBefore(end1) || end2.equals(end1)) ||
                   (start2.isBefore(end1) && end2.isAfter(start1));
        }
        
        if (!window1CrossesMidnight) {
            // Window 2 crosses midnight, window 1 doesn't
            return start1.isBefore(LocalTime.MAX) && (start1.isAfter(start2) || start1.equals(start2)) ||
                   end1.isAfter(LocalTime.MIN) && (end1.isBefore(end2) || end1.equals(end2)) ||
                   (start1.isBefore(end2) && end1.isAfter(start2));
        }
        
        // Both windows cross midnight - they always overlap
        return true;
    }
    
    /**
     * Checks if two time windows overlap with a service date context
     * More accurate as it considers the actual dates
     * 
     * @param date1 Service date for first trip
     * @param start1 Start time of first trip
     * @param end1 End time of first trip
     * @param date2 Service date for second trip
     * @param start2 Start time of second trip
     * @param end2 End time of second trip
     * @return true if trips overlap in actual time
     */
    public static boolean hasOverlapWithDate(
            LocalDate date1,
            LocalTime start1,
            LocalTime end1,
            LocalDate date2,
            LocalTime start2,
            LocalTime end2) {
        
        if (date1 == null || date2 == null || 
            start1 == null || end1 == null || 
            start2 == null || end2 == null) {
            return false;
        }
        
        // Convert to LocalDateTime for accurate comparison
        LocalDateTime start1DateTime = LocalDateTime.of(date1, start1);
        LocalDateTime end1DateTime = end1.isBefore(start1) 
            ? LocalDateTime.of(date1.plusDays(1), end1)  // Crosses midnight
            : LocalDateTime.of(date1, end1);
        
        LocalDateTime start2DateTime = LocalDateTime.of(date2, start2);
        LocalDateTime end2DateTime = end2.isBefore(start2)
            ? LocalDateTime.of(date2.plusDays(1), end2)  // Crosses midnight
            : LocalDateTime.of(date2, end2);
        
        // Standard DateTime overlap check
        return start1DateTime.isBefore(end2DateTime) && start2DateTime.isBefore(end1DateTime);
    }
    
    /**
     * Calculates the gap between two trip times (from end of first to start of second)
     * Accounts for midnight crossing
     * 
     * @param firstEnd End time of first trip
     * @param secondStart Start time of second trip
     * @return Gap in minutes (can be negative if times overlap)
     */
    public static long calculateGapMinutes(LocalTime firstEnd, LocalTime secondStart) {
        if (firstEnd == null || secondStart == null) {
            return 0;
        }
        
        // If second start is before first end, it might be next day
        if (secondStart.isBefore(firstEnd)) {
            // Assume crosses midnight
            Duration untilMidnight = Duration.between(firstEnd, LocalTime.MAX);
            Duration afterMidnight = Duration.between(LocalTime.MIN, secondStart);
            return untilMidnight.toMinutes() + afterMidnight.toMinutes() + 1;
        }
        
        // Normal case - same day
        return Duration.between(firstEnd, secondStart).toMinutes();
    }
    
    /**
     * Validates minimum gap between two trips, accounting for midnight
     * 
     * @param trip1Departure Departure time of first trip
     * @param trip2Departure Departure time of second trip
     * @param minimumGapMinutes Required minimum gap
     * @return true if gap requirement is satisfied
     */
    public static boolean satisfiesMinimumGap(
            LocalTime trip1Departure,
            LocalTime trip2Departure,
            int minimumGapMinutes) {
        
        if (trip1Departure == null || trip2Departure == null) {
            return false;
        }
        
        long gapMinutes = Math.abs(calculateDurationMinutes(trip1Departure, trip2Departure));
        return gapMinutes >= minimumGapMinutes;
    }
    
    /**
     * Checks if a time is within a time window, accounting for midnight crossing
     */
    public static boolean isTimeInWindow(LocalTime time, LocalTime windowStart, LocalTime windowEnd) {
        if (time == null || windowStart == null || windowEnd == null) {
            return false;
        }
        
        boolean crossesMidnight = windowEnd.isBefore(windowStart);
        
        if (crossesMidnight) {
            // Window spans midnight: valid if time >= start OR time <= end
            return !time.isBefore(windowStart) || !time.isAfter(windowEnd);
        } else {
            // Normal window: valid if start <= time <= end
            return !time.isBefore(windowStart) && !time.isAfter(windowEnd);
        }
    }
}
