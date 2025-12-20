package lk.ashan.routenetlkserverapllication.module.driver.controller;


import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.driver.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.driver.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.driver.service.DriverService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<DriverDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<DriverDetailResponseDto> drivers = params.isEmpty()
                ?driverService.getDrivers()
                : driverService.searchDriver(params);
        return APIResponseBuilder.getResponse(drivers, drivers.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<DriverDetailResponseDto>> add(
            @RequestBody @Valid DriverCreateRequestDto driverCreateRequestDto)
    {
        DriverDetailResponseDto savedDriver = driverService.createDriver(driverCreateRequestDto);
        return APIResponseBuilder.postResponse(savedDriver, savedDriver.getId());
    }

}
