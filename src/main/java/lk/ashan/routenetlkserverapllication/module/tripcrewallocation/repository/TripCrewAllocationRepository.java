package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripcrewallocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TripCrewAllocationRepository extends JpaRepository<Tripcrewallocation, Integer> {

    /**
     * Find all allocations for a specific trip.
     */
    List<Tripcrewallocation> findByTrip_Id(Integer tripId);

    /**
     * Find allocations by service date and status.
     * Used to check for existing allocations when generating suggestions.
     */
    @Query("SELECT t FROM Tripcrewallocation t " +
            "WHERE t.trip.doservice = :serviceDate " +
            "AND t.tripallocationstatus.name IN :statusNames")
    List<Tripcrewallocation> findByTrip_DoserviceAndTripallocationstatus_NameIn(
            @Param("serviceDate") LocalDate serviceDate,
            @Param("statusNames") List<String> statusNames
    );

    /**
     * Find allocations for a specific employee on a specific date.
     * Used to prevent double-booking.
     */
    @Query("SELECT t FROM Tripcrewallocation t " +
            "WHERE t.employee.id = :employeeId " +
            "AND t.trip.doservice = :serviceDate " +
            "AND t.tripallocationstatus.name IN :statusNames")
    List<Tripcrewallocation> findByEmployee_IdAndTrip_DoserviceAndTripallocationstatus_NameIn(
            @Param("employeeId") Integer employeeId,
            @Param("serviceDate") LocalDate serviceDate,
            @Param("statusNames") List<String> statusNames
    );

    /**
     * Check if employee has overlapping trip assignment.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Tripcrewallocation t " +
            "WHERE t.employee.id = :employeeId " +
            "AND t.trip.doservice = :serviceDate " +
            "AND t.tripallocationstatus.name IN ('Suggested', 'Confirmed', 'Auto-assigned') " +
            "AND t.trip.todepature < :arrivalTime " +
            "AND t.trip.toarrival > :departureTime")
    boolean existsOverlappingAllocation(
            @Param("employeeId") Integer employeeId,
            @Param("serviceDate") LocalDate serviceDate,
            @Param("departureTime") LocalTime departureTime,
            @Param("arrivalTime") LocalTime arrivalTime
    );

    /**
     * Delete all suggestions for a trip (used when regenerating).
     */
    @Modifying
    @Query("DELETE FROM Tripcrewallocation t " +
            "WHERE t.trip.id = :tripId " +
            "AND t.tripallocationstatus.name = 'Suggested'")
    void deleteSuggestionsByTripId(@Param("tripId") Integer tripId);

    List<Tripcrewallocation> findByTrip_DoserviceBetween(LocalDate startDate, LocalDate endDate);
}
