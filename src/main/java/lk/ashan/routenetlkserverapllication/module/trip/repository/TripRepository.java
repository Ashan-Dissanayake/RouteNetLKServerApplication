package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

    List<Trip> findByPermite_Route_IdAndOriginterminal_IdAndDoservice
            (Integer permite_route_id,
             Integer originterminal_id,
             LocalDate doservice
            );

    List<Trip> findByPermite_IdAndDoservice(Integer permitId,LocalDate doService);

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
    FROM Trip t
    LEFT JOIN Tripvehicleoverride o ON o.trip.id = t.id
    WHERE (
            o.vehicle.id = :vehicleId
            OR t.permite.vehicle.id = :vehicleId
          )
      AND t.id <> :currentTripId
      AND t.tripstatus.name IN ('READY','IN_PROGRESS','DELAYED','SUSPENDED')
      AND t.todepature < :arrival
      AND t.toarrival > :departure
""")
    boolean existsVehicleConflictForOverride(
            Integer vehicleId,
            LocalTime departure,
            LocalTime arrival,
            Integer currentTripId
    );

    List<Trip> findByDoserviceAndTripstatus_NameIn(LocalDate doservice, List<String> statusNames);


    @Query("SELECT MAX(t.notrip) FROM Trip t WHERE t.permite.id = :permitId AND t.doservice = :serviceDate")
    Optional<Integer> findMaxTripNumberForPermitAndDate(Integer permitId, LocalDate serviceDate);

    List<Trip> findByDoserviceAndBranchId(LocalDate doservice, Integer branchId);

    @Query("SELECT t FROM Trip t " +
            "JOIN FETCH t.permite p " +
            "JOIN FETCH p.route r " +
            "JOIN FETCH r.routetype rt " + // Navigate the direct FK link
            "WHERE t.doservice = :date " +
            "AND rt.name = 'Inter provincial' " +
            "AND t.tripstatus.name IN ('Planned', 'Ready')")
    List<Trip> findInterprovincialTrips(@Param("date") LocalDate date);
}
