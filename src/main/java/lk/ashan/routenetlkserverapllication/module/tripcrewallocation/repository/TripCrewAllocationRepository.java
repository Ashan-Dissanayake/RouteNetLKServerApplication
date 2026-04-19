package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TripCrewAllocationRepository extends JpaRepository<TripCrewAllocation, Integer> {
    @Modifying
    @Query("DELETE FROM TripCrewAllocation tca " +
            "WHERE tca.trip.doservice = :date " +
            "AND tca.trip.branch.id = :branchId")
    void deleteByTripDateAndBranchId(@Param("date") LocalDate date, @Param("branchId") Integer branchId);
}
