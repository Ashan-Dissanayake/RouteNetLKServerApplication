package lk.ashan.routenetlkserverapllication.module.sparepart.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.service.PartService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing spare parts.
 * Provides endpoints for viewing, adding, updating, and deactivating parts.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    /**
     * Retrieves a list of parts based on the provided parameters.
     *
     * @param params A map of query parameters for filtering parts.
     * @return A response entity containing a list of part details.
     */
    @PreAuthorize("hasAuthority('part-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PartDetailResponseDto> parts = params.isEmpty()
                ? partService.getParts()
                : partService.searchParts(params);

        return APIResponseBuilder.list(parts, parts.size());
    }

    /**
     * Retrieves a summary list of all parts.
     *
     * @return A response entity containing a list of part summaries.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartSummaryDto>>> get() {
        List<PartSummaryDto> parts =  partService.getSummaryParts();
        return APIResponseBuilder.list(parts, parts.size());
    }

    /**
     * Adds a new part to the system.
     *
     * @param partRequest The request body containing part creation details.
     * @return A response entity containing the details of the created part.
     */
    @PreAuthorize("hasAuthority('part-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<PartDetailResponseDto>> add(
            @RequestBody @Valid PartCreateRequestDto partRequest
    ) {
        PartDetailResponseDto savedPart = partService.createPart(partRequest);
        return APIResponseBuilder.created(savedPart, savedPart.getId());
    }

    /**
     * Updates an existing part in the system.
     *
     * @param partUpdateRequest The request body containing part update details.
     * @return A response entity containing the details of the updated part.
     */
    @PreAuthorize("hasAuthority('part-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<PartDetailResponseDto>> update(
            @RequestBody @Valid PartUpdateRequestDto partUpdateRequest
    ) {
        PartDetailResponseDto updatedPart = partService.updatePart(partUpdateRequest);
        return APIResponseBuilder.updated(updatedPart, updatedPart.getId());
    }

    /**
     * Deactivates a list of parts based on their IDs.
     *
     * @param ids A list of part IDs to deactivate.
     * @return A response entity containing the list of deactivated part IDs and additional metadata.
     */
    @PreAuthorize("hasAuthority('part-delete')")
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
