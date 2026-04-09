package lk.ashan.routenetlkserverapllication.module.grn.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.service.GrnService;
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
@RequestMapping(value = "/grns")
@RequiredArgsConstructor
public class GrnController {

    private final GrnService grnService;

    @PreAuthorize("hasAuthority('grn-select')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<GrnDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<GrnDetailResponseDto> requests = params.isEmpty()
                ? grnService.getGrns()
                : grnService.searchGrns(params);

        return APIResponseBuilder.list(requests, requests.size());
    }

    @PreAuthorize("hasAuthority('grn-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<GrnDetailResponseDto>> update(
            @RequestBody @Valid GrnUpdateRequestDto dto
    ) {
        GrnDetailResponseDto updated = grnService.updateGrn(dto);
        return APIResponseBuilder.updated(updated, updated.getId());
    }

}
