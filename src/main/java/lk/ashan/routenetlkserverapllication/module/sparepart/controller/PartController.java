package lk.ashan.routenetlkserverapllication.module.sparepart.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.service.PartService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PartDetailResponseDto> parts = params.isEmpty()
                ? partService.getParts()
                : partService.searchParts(params);

        return APIResponseBuilder.list(parts, parts.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<PartDetailResponseDto>> add(
            @RequestBody @Valid PartCreateRequestDto partRequest
    ) {
        PartDetailResponseDto savedPart = partService.createPart(partRequest);
        return APIResponseBuilder.created(savedPart, savedPart.getId());
    }

    @PutMapping
    public ResponseEntity<APISuccessResponse<PartDetailResponseDto>> update(
            @RequestBody @Valid PartUpdateRequestDto partUpdateRequest
    ) {
        PartDetailResponseDto updatedPart = partService.updatePart(partUpdateRequest);
        return APIResponseBuilder.updated(updatedPart, updatedPart.getId());
    }

    @PostMapping("/deactivate")
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivate(
            @RequestBody List<Integer> ids
    ) {
        List<Integer> deactivatedIds = partService.deactivateParts(ids);
        return APIResponseBuilder.ok(
                deactivatedIds,
                Map.of("status", "deactivated", "count", deactivatedIds.size())
        );
    }

}
