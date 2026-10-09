package lk.ashan.routenetlkserverapllication.module.permit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.service.PermitService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing permits. Provides endpoints for viewing, adding, and transferring permits.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/permits")
@RequiredArgsConstructor
@Tag(name = "Permit Management", description = "Endpoints for managing permits in the system.")
public class PermitController {

    private final PermitService permitService;

    /**
     * Retrieves a list of permits. If parameters are provided, performs a search based on the parameters.
     *
     * @param params A map of query parameters for filtering permits.
     * @return A ResponseEntity containing a list of PermitDetailResponseDto objects and their count.
     */
    @Operation(summary = "Get all permits or search by parameters", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved permits"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'permit-view' authority")
    })
    @PreAuthorize("hasAuthority('permit-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PermitDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PermitDetailResponseDto> permits = params.isEmpty()
                ? permitService.getPermits()
                : permitService.searchPermit(params);
        return APIResponseBuilder.list(permits, permits.size());
    }

    /**
     * Retrieves a summary list of permits.
     *
     * @return A ResponseEntity containing a list of PermitSummaryResponseDto objects and their count.
     */
    @Operation(summary = "Get permit summaries for dropdowns", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved summaries"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PermitSummaryResponseDto>>> get() {
        List<PermitSummaryResponseDto> permits = permitService.getSummaryPermits();
        return APIResponseBuilder.list(permits, permits.size());
    }

    /**
     * Creates a new permit.
     *
     * @param permitCreateRequestDto The details of the permit to be created.
     * @return A ResponseEntity containing the created PermitDetailResponseDto object.
     */
    @Operation(summary = "Add a new permit", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permit created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'permit-add' authority")
    })
    @PreAuthorize("hasAuthority('permit-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<PermitDetailResponseDto>> add(
            @RequestBody @Valid PermitCreateRequestDto permitCreateRequestDto)
    {
        PermitDetailResponseDto savedPermit = permitService.createPermit(permitCreateRequestDto);
        return APIResponseBuilder.list(savedPermit, savedPermit.getId());
    }

    /**
     * Transfers a permit to another entity.
     *
     * @param permitId The ID of the permit to be transferred.
     * @return A ResponseEntity containing the updated PermitDetailResponseDto object.
     */
    @Operation(summary = "Transfer a permit", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permit transferred successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'permit-transfer' authority"),
            @ApiResponse(responseCode = "404", description = "Permit not found")
    })
    @PreAuthorize("hasAuthority('permit-transfer')")
    @PutMapping("/transfer/{permitId}")
    public ResponseEntity<APISuccessResponse<PermitDetailResponseDto>> transferPermit(
            @PathVariable Integer permitId
    ) {
        PermitDetailResponseDto updatedPermit = permitService.transferPermit(permitId);
        return APIResponseBuilder.created(updatedPermit, updatedPermit.getId());
    }
}
