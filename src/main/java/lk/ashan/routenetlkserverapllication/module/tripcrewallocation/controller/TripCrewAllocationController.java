package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.controller;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.dto.TripCrewAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service.TripCrewAllocationService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/trip-crew-allocations")
@RequiredArgsConstructor
public class TripCrewAllocationController {

    private final TripCrewAllocationService tripCrewAllocationService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripCrewAllocationDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<TripCrewAllocationDetailResponseDto> trips = params.isEmpty()
                ? tripCrewAllocationService.getTripAllocations()
                : tripCrewAllocationService.searchTripCrewAllocations(params);
        return APIResponseBuilder.list(trips, trips.size());
    }
}
