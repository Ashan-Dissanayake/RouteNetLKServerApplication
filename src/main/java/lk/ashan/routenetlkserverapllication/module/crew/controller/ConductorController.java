package lk.ashan.routenetlkserverapllication.module.crew.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.crew.dto.*;
import lk.ashan.routenetlkserverapllication.module.crew.service.ConductorService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/conductors")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ConductorDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<ConductorDetailResponseDto> conductors = params.isEmpty()
                ?conductorService.getConductors()
                : conductorService.searchConductor(params);
        return APIResponseBuilder.getResponse(conductors, conductors.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<ConductorDetailResponseDto>> add(
            @RequestBody @Valid ConductorCreateRequestDto conductorCreateRequestDto)
    {
        ConductorDetailResponseDto savedConductor = conductorService.createConductor(conductorCreateRequestDto);
        return APIResponseBuilder.postResponse(savedConductor, savedConductor.getId());
    }
    
    @PutMapping
    public ResponseEntity<APISuccessResponse<ConductorDetailResponseDto>> update(
            @RequestBody @Valid ConductorUpdateRequestDto conductorUpdateRequestDto)
    {
        ConductorDetailResponseDto updateConductor = conductorService.updateConductor(conductorUpdateRequestDto);
        return APIResponseBuilder.postResponse(updateConductor, updateConductor.getId());
    }

}
