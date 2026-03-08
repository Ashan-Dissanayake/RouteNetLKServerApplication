package lk.ashan.routenetlkserverapllication.module.partreqest.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.service.PartRequestService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/partrequests")
@RequiredArgsConstructor
public class PartRequestController {

    private final PartRequestService partRequestService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartRequestDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PartRequestDetailResponseDto> requests = params.isEmpty()
                ? partRequestService.getPartRequests()
                : partRequestService.searchPartRequests(params);

        return APIResponseBuilder.list(requests, requests.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> create(
            @RequestBody @Valid PartRequestCreateRequestDto dto
    ) {
        PartRequestDetailResponseDto savedRequest = partRequestService.createRequest(dto);
        return APIResponseBuilder.created(savedRequest, savedRequest.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> update(
            @RequestBody @Valid PartRequestUpdateRequestDto dto
    ) {
        PartRequestDetailResponseDto updatedRequest = partRequestService.updateRequest(dto);
        return APIResponseBuilder.updated(updatedRequest, updatedRequest.getId());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> approve(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.approveRequest(id);
        return APIResponseBuilder.ok(request);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> reject(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.rejectRequest(id);
        return APIResponseBuilder.ok(request);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<APISuccessResponse<PartRequestDetailResponseDto>> complete(
            @PathVariable Integer id
    ) {
        PartRequestDetailResponseDto request = partRequestService.completeRequest(id);
        return APIResponseBuilder.ok(request);
    }

}
