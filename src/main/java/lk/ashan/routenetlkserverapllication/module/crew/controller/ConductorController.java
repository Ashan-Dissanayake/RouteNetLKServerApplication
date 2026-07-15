package lk.ashan.routenetlkserverapllication.module.crew.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.ConductorService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing conductors. Provides endpoints for retrieving, adding, and updating conductors.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/conductors")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    /**
     * Retrieves a list of conductors. If parameters are provided, it performs a search based on the parameters.
     *
     * @param params A map of search parameters to filter conductors.
     * @return A ResponseEntity containing a list of conductors and their details.
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority.
     */
    @PreAuthorize("hasAuthority('conductor-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ConductorDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<ConductorDetailResponseDto> conductors = params.isEmpty()
                ? conductorService.getConductors()
                : conductorService.searchConductor(params);
        return APIResponseBuilder.list(conductors, conductors.size());
    }

    /**
     * Adds a new conductor.
     *
     * @param conductorCreateRequestDto The details of the conductor to be added.
     * @return A ResponseEntity containing the details of the newly created conductor.
     * @throws jakarta.validation.ConstraintViolationException if the request body validation fails.
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority.
     */
    @PreAuthorize("hasAuthority('conductor-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<ConductorDetailResponseDto>> add(
            @RequestBody @Valid ConductorCreateRequestDto conductorCreateRequestDto)
    {
        ConductorDetailResponseDto savedConductor = conductorService.createConductor(conductorCreateRequestDto);
        return APIResponseBuilder.created(savedConductor, savedConductor.getId());
    }

    /**
     * Updates an existing conductor.
     *
     * @param conductorUpdateRequestDto The updated details of the conductor.
     * @return A ResponseEntity containing the updated conductor details.
     * @throws jakarta.validation.ConstraintViolationException if the request body validation fails.
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority.
     */
    @PreAuthorize("hasAuthority('conductor-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<ConductorDetailResponseDto>> update(
            @RequestBody @Valid ConductorUpdateRequestDto conductorUpdateRequestDto)
    {
        ConductorDetailResponseDto updateConductor = conductorService.updateConductor(conductorUpdateRequestDto);
        return APIResponseBuilder.updated(updateConductor, updateConductor.getId());
    }

}
