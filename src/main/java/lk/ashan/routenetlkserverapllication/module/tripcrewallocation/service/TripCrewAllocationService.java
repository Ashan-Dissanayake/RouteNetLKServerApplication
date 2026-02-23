package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.dto.TripCrewAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.mapper.TripCrewAllocationMapper;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.Tripcrewallocation;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository.TripCrewAllocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TripCrewAllocationService {

    private final TripCrewAllocationRepository tripCrewAllocationRepository;
    private final TripCrewAllocationMapper tripCrewAllocationMapper;

    @Transactional(readOnly = true)
    public List<TripCrewAllocationDetailResponseDto> getTripAllocations() {
        return tripCrewAllocationMapper.toDtoList(tripCrewAllocationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TripCrewAllocationDetailResponseDto> searchTripCrewAllocations(@NotNull HashMap<String, String> params) {

        List<Tripcrewallocation> tripCrewAllocations = tripCrewAllocationRepository.findAll();

        if (!params.isEmpty()) {

            String tripId = params.get("sstripid");
            String tripAllocationStatusId = params.get("sstripallocationstatusid");

            Stream<Tripcrewallocation> tripcrewallocationStream = tripCrewAllocations.stream();

            if (tripId != null)
                tripcrewallocationStream = tripcrewallocationStream.filter(t -> t.getTrip().getId() == Integer.parseInt(tripId));
            if (tripAllocationStatusId != null)
                tripcrewallocationStream = tripcrewallocationStream.filter(t -> t.getTripallocationstatus().getId() == Integer.parseInt(tripAllocationStatusId));

            return tripCrewAllocationMapper.toDtoList(tripcrewallocationStream.collect(Collectors.toList()));
        }

        return tripCrewAllocationMapper.toDtoList(tripCrewAllocations);
    }
}
