package lk.ashan.routenetlkserverapllication.module.sparepart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Spare Part Management", description = "Endpoints for managing spare parts in the inventory.")
public class PartController {

    private final PartService partService;

    /**
     * Retrieves a list of parts based on the provided parameters.
     *
     * @param params A map of query parameters for filtering parts.
     * @return A response entity containing a list of part details.
     */
    @Operation(summary = "Get all parts or search by parameters", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved parts"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'spare-part-view' authority")
    })
    @PreAuthorize("hasAuthority('spare-part-view')")
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
    @Operation(summary = "Get part summaries for dropdowns", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved summaries"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(summary = "Add a new spare part", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Part created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'spare-part-add' authority")
    })
    @PreAuthorize("hasAuthority('spare-part-add')")
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
    @Operation(summary = "Update an existing spare part", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'spare-part-update' authority"),
            @ApiResponse(responseCode = "404", description = "Part not found")
    })
    @PreAuthorize("hasAuthority('spare-part-update')")
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
    @Operation(summary = "Deactivate multiple parts", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parts deactivated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'spare-part-delete' authority")
    })
    @PreAuthorize("hasAuthority('spare-part-delete')")
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
