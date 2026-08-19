package lk.ashan.routenetlkserverapllication.module.permit.controller;

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
public class PermitController {

    private final PermitService permitService;

    /**
     * Retrieves a list of permits. If parameters are provided, performs a search based on the parameters.
     *
     * @param params A map of query parameters for filtering permits.
     * @return A ResponseEntity containing a list of PermitDetailResponseDto objects and their count.
     */
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
    @PreAuthorize("hasAuthority('permit-transfer')")
    @PutMapping("/transfer/{permitId}")
    public ResponseEntity<APISuccessResponse<PermitDetailResponseDto>> transferPermit(
            @PathVariable Integer permitId
    ) {
        PermitDetailResponseDto updatedPermit = permitService.transferPermit(permitId);
        return APIResponseBuilder.created(updatedPermit, updatedPermit.getId());
    }
}
