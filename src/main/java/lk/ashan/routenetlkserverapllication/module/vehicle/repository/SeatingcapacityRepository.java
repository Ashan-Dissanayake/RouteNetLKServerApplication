package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Make;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Seatingcapacity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatingcapacityRepository extends JpaRepository<Seatingcapacity, Integer> {

    List<Seatingcapacity> findByMakeId(Integer id);
}
