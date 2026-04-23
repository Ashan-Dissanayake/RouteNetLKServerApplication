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


    boolean existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrival(Integer permitId, Integer originTerminalId, LocalTime departure, LocalTime arrival);
}
