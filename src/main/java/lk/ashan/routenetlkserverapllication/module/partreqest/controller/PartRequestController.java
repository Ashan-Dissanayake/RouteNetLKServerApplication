package lk.ashan.routenetlkserverapllication.module.partreqest.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestSummaryDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.service.PartRequestService;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing part requests.
 * Provides endpoints for viewing, creating, updating, approving, and rejecting part requests.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/part-requests")
@RequiredArgsConstructor
public class PartRequestController {

    private final PartRequestService partRequestService;

    /**
     * Retrieves a list of part requests based on the provided parameters.
     *
     * @param params A map of query parameters for filtering part requests.
     * @return A response entity containing a list of part request details.
     */
    @PreAuthorize("hasAuthority('part-request-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartRequestDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PartRequestDetailResponseDto> requests = params.isEmpty()
                ? partRequestService.getPartRequests()
                : partRequestService.searchPartRequests(params);

        return APIResponseBuilder.list(requests, requests.size());
    }

    /**
     * Retrieves a summary of all part requests.
     *
     * @return A response entity containing a list of part request summaries.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartRequestSummaryDto>>> get() {
        List<PartRequestSummaryDto> partRequests =  partRequestService.getSummaryPartRequests();
        return APIResponseBuilder.list(partRequests, partRequests.size());
    }

    /**
     * Creates a new part request.
     *
     * @param dto The data transfer object containing details for the new part request.
     * @return A response entity containing the created part request details.
     */
    @PreAuthorize("hasAuthority('part-request-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> create(
            @RequestBody @Valid PartRequestCreateRequestDto dto
    ) {
        PartRequestDetailResponseDto savedRequest = partRequestService.createRequest(dto);
        return APIResponseBuilder.created(savedRequest, savedRequest.getId());
    }

    /**
     * Updates an existing part request.
     *
     * @param dto The data transfer object containing updated details for the part request.
     * @return A response entity containing the updated part request details.
     */
    @PreAuthorize("hasAuthority('part-request-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> update(
            @RequestBody @Valid PartRequestUpdateRequestDto dto
    ) {
        PartRequestDetailResponseDto updatedRequest = partRequestService.updateRequest(dto);
        return APIResponseBuilder.updated(updatedRequest, updatedRequest.getId());
    }

    /**
     * Approves a part request.
     *
     * @param id The ID of the part request to approve.
     * @return A response entity containing the approved part request details.
     */
    @PreAuthorize("hasAuthority('part-request-approve')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> approve(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.approveRequest(id);
        return APIResponseBuilder.ok(request);
    }

    /**
     * Rejects a part request.
     *
     * @param id The ID of the part request to reject.
     * @return A response entity containing the rejected part request details.
     */
    @PreAuthorize("hasAuthority('part-request-reject')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> reject(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.rejectRequest(id);
        return APIResponseBuilder.ok(request);
    }
}
