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

@CrossOrigin
@RestController
@RequestMapping(value = "/conductors")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    @PreAuthorize("hasAuthority('conductor-select')")
    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ConductorDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<ConductorDetailResponseDto> conductors = params.isEmpty()
                ?conductorService.getConductors()
                : conductorService.searchConductor(params);
        return APIResponseBuilder.list(conductors, conductors.size());
    }

    @PreAuthorize("hasAuthority('conductor-insert')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<ConductorDetailResponseDto>> add(
            @RequestBody @Valid ConductorCreateRequestDto conductorCreateRequestDto)
    {
        ConductorDetailResponseDto savedConductor = conductorService.createConductor(conductorCreateRequestDto);
        return APIResponseBuilder.created(savedConductor, savedConductor.getId());
    }

    @PreAuthorize("hasAuthority('conductor-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<ConductorDetailResponseDto>> update(
            @RequestBody @Valid ConductorUpdateRequestDto conductorUpdateRequestDto)
    {
        ConductorDetailResponseDto updateConductor = conductorService.updateConductor(conductorUpdateRequestDto);
        return APIResponseBuilder.updated(updateConductor, updateConductor.getId());
    }

}
