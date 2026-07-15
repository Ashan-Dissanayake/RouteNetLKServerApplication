package lk.ashan.routenetlkserverapllication.module.crew.controller;


import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.DriverService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing driver-related operations.
 * Provides endpoints for retrieving, adding, and updating driver information.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    /**
     * Retrieves a list of drivers. If query parameters are provided, performs a search based on the parameters.
     *
     * @param params A map of query parameters for filtering drivers.
     * @return A ResponseEntity containing a list of DriverDetailResponseDto objects and the total count.
     * @throws SecurityException if the user does not have the 'driver-select' authority.
     */
    @PreAuthorize("hasAuthority('driver-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<DriverDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<DriverDetailResponseDto> drivers = params.isEmpty()
                ? driverService.getDrivers()
                : driverService.searchDriver(params);
        return APIResponseBuilder.list(drivers, drivers.size());
    }

    /**
     * Adds a new driver to the system.
     *
     * @param driverCreateRequestDto The data transfer object containing the details of the driver to be created.
     * @return A ResponseEntity containing the created DriverDetailResponseDto and its ID.
     * @throws SecurityException if the user does not have the 'branch-insert' authority.
     * @throws jakarta.validation.ConstraintViolationException if the input data is invalid.
     */
    @PreAuthorize("hasAuthority('branch-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<DriverDetailResponseDto>> add(
            @RequestBody @Valid DriverCreateRequestDto driverCreateRequestDto)
    {
        DriverDetailResponseDto savedDriver = driverService.createDriver(driverCreateRequestDto);
        return APIResponseBuilder.created(savedDriver, savedDriver.getId());
    }

    /**
     * Updates an existing driver's information.
     *
     * @param driverUpdateRequestDto The data transfer object containing the updated details of the driver.
     * @return A ResponseEntity containing the updated DriverDetailResponseDto and its ID.
     * @throws SecurityException if the user does not have the 'branch-update' authority.
     * @throws jakarta.validation.ConstraintViolationException if the input data is invalid.
     */
    @PreAuthorize("hasAuthority('branch-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<DriverDetailResponseDto>> update(
            @RequestBody @Valid DriverUpdateRequestDto driverUpdateRequestDto)
    {
        DriverDetailResponseDto updateDriver = driverService.updateDriver(driverUpdateRequestDto);
        return APIResponseBuilder.updated(updateDriver, updateDriver.getId());
    }

}
