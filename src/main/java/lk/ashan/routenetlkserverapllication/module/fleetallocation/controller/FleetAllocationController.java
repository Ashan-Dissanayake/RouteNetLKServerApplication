package lk.ashan.routenetlkserverapllication.module.fleetallocation.controller;

import lk.ashan.routenetlkserverapllication.module.fleetallocation.dto.FleetAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.fleetallocation.service.FleetAllocationService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/fleetallocations")
@RequiredArgsConstructor
public class FleetAllocationController {

    private final FleetAllocationService fleetAllocationService;


    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<FleetAllocationDetailResponseDto>>> getAllocationsByDate(
            @RequestParam HashMap<String,String > params) {

        List<FleetAllocationDetailResponseDto> response =
                fleetAllocationService.getAllocationsByDate(LocalDate.parse(params.get("date")));

        return APIResponseBuilder.getResponse(response,response.size());
    }
}
