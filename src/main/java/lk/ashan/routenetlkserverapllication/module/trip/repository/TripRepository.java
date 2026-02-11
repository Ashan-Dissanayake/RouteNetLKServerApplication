package lk.ashan.routenetlkserverapllication.module.trip.repository;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

    List<Trip> findByPermite_Route_IdAndOriginterminal_IdAndDoservice
            (Integer permite_route_id,
             Integer originterminal_id,
             LocalDate doservice
            );

    List<Trip> findByPermite_IdAndDoservice(Integer permitId,LocalDate doService);

}
