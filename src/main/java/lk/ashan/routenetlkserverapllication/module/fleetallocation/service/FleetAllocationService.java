package lk.ashan.routenetlkserverapllication.module.fleetallocation.service;

import lk.ashan.routenetlkserverapllication.module.fleetallocation.dto.FleetAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.fleetallocation.mapper.FleetAllocationMapper;
import lk.ashan.routenetlkserverapllication.module.fleetallocation.model.Fleetallocation;
import lk.ashan.routenetlkserverapllication.module.fleetallocation.repository.FleetallocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FleetAllocationService {

    private final FleetallocationRepository fleetAllocationRepository;
    private final FleetAllocationMapper fleetAllocationMapper;

    public List<FleetAllocationDetailResponseDto> getAllocationsByDate(LocalDate operationDate) {

        List<Fleetallocation> allocations =
                fleetAllocationRepository.findByRoster_Doroster(operationDate);

        return allocations.stream()
                .map(fleetAllocationMapper::toDetailDto)
                .toList();
    }
}
