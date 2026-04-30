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

    List<Trip> findByOriginterminal_Id(Integer originterminalId);


    boolean existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(Integer permitId, Integer originTerminalId, LocalTime departure, LocalTime arrival, String status);

    long countByPermite_IdAndTripstatus_Name(Integer permitId, String active);

    List<Trip> findByPermite_IdAndTripstatus_Name(Integer permitId, String active);


    @Query("SELECT COUNT(DISTINCT t.permite.id) FROM Trip t " +
            "WHERE t.permite.branch.id = :branchId " +
            "AND t.tripstatus.name = 'Active' " +
            "AND t.todepature >= :startTime " +
            "AND t.todepature < :endTime")
    long countDistinctPermitsForShift(
            @Param("branchId") Integer branchId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT COUNT(t) > 0 FROM Trip t " +
            "JOIN t.permite p " +
            "JOIN p.route r " +
            "WHERE t.branch.id = :branchId " +
            "AND r.routetype.id = 2 " + // 2 = Interprovincial/High Skill
            "AND t.todepature >= :shiftStart " +
            "AND t.todepature < :shiftEnd")
    boolean existsInterprovincialTripInShift(
            @Param("branchId") Integer branchId,
            @Param("shiftStart") LocalTime shiftStart,
            @Param("shiftEnd") LocalTime shiftEnd
    );

}
