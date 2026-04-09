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

@CrossOrigin
@RestController
@RequestMapping(value = "/part-requests")
@RequiredArgsConstructor
public class PartRequestController {

    private final PartRequestService partRequestService;

    @PreAuthorize("hasAuthority('part-requests-select')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartRequestDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PartRequestDetailResponseDto> requests = params.isEmpty()
                ? partRequestService.getPartRequests()
                : partRequestService.searchPartRequests(params);

        return APIResponseBuilder.list(requests, requests.size());
    }

    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartRequestSummaryDto>>> get() {
        List<PartRequestSummaryDto> partRequests =  partRequestService.getSummaryPartRequests();
        return APIResponseBuilder.list(partRequests, partRequests.size());
    }


    @PreAuthorize("hasAuthority('part-requests-insert')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> create(
            @RequestBody @Valid PartRequestCreateRequestDto dto
    ) {
        PartRequestDetailResponseDto savedRequest = partRequestService.createRequest(dto);
        return APIResponseBuilder.created(savedRequest, savedRequest.getId());
    }

    @PreAuthorize("hasAuthority('part-requests-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> update(
            @RequestBody @Valid PartRequestUpdateRequestDto dto
    ) {
        PartRequestDetailResponseDto updatedRequest = partRequestService.updateRequest(dto);
        return APIResponseBuilder.updated(updatedRequest, updatedRequest.getId());
    }

    @PreAuthorize("hasAuthority('part-requests-update')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> approve(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.approveRequest(id);
        return APIResponseBuilder.ok(request);
    }

    @PreAuthorize("hasAuthority('part-requests-update')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> reject(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.rejectRequest(id);
        return APIResponseBuilder.ok(request);
    }

    /*
    @PreAuthorize("hasAuthority('part-requests-update')")
    @PostMapping("/{id}/complete")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> complete(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.completeRequest(id);
        return APIResponseBuilder.ok(request);
    }
    */
}
