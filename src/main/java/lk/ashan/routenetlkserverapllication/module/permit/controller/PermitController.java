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

@CrossOrigin
@RestController
@RequestMapping(value = "/permits")
@RequiredArgsConstructor
public class PermitController {
    
    private final PermitService permitService;

    @PreAuthorize("hasAuthority('permit-view')")
    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PermitDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PermitDetailResponseDto> permits = params.isEmpty()
                ?permitService.getPermits()
                : permitService.searchPermit(params);
        return APIResponseBuilder.list(permits, permits.size());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PermitSummaryResponseDto>>> get() {
        List<PermitSummaryResponseDto> permits =  permitService.getSummaryPermits();
        return APIResponseBuilder.list(permits, permits.size());
    }


    @PreAuthorize("hasAuthority('permit-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<PermitDetailResponseDto>> add(
            @RequestBody @Valid PermitCreateRequestDto permitCreateRequestDto)
    {
        PermitDetailResponseDto savedPermit = permitService.createPermit(permitCreateRequestDto);
        return APIResponseBuilder.list(savedPermit, savedPermit.getId());
    }

    @PreAuthorize("hasAuthority('permit-transfer')")
    @PutMapping("/transfer/{permitId}")
    public ResponseEntity<APISuccessResponse<PermitDetailResponseDto>>  transferPermit(
            @PathVariable Integer permitId
    ) {
        PermitDetailResponseDto updatedPermit = permitService.transferPermit(permitId);
        return APIResponseBuilder.created(updatedPermit, updatedPermit.getId());
    }
}
