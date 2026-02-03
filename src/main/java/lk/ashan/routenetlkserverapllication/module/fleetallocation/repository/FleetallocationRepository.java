package lk.ashan.routenetlkserverapllication.module.fleetallocation.repository;

import lk.ashan.routenetlkserverapllication.module.fleetallocation.dto.FleetAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.fleetallocation.model.Fleetallocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface FleetallocationRepository extends JpaRepository<Fleetallocation, Integer> {

    List<Fleetallocation> findByRoster_Doroster(LocalDate doroster);


}
