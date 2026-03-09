package lk.ashan.routenetlkserverapllication.module.grn.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.service.GrnService;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/grns")
@RequiredArgsConstructor
public class GrnController {

    private final GrnService grnService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<GrnDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<GrnDetailResponseDto> requests = params.isEmpty()
                ? grnService.getGrns()
                : grnService.searchGrns(params);

        return APIResponseBuilder.list(requests, requests.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<GrnDetailResponseDto>> create(
            @RequestBody @Valid GrnCreateRequestDto dto
    ) {
        GrnDetailResponseDto saved = grnService.createGrn(dto);
        return APIResponseBuilder.created(saved, saved.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<APISuccessResponse<GrnDetailResponseDto>> update(
            @RequestBody @Valid GrnUpdateRequestDto dto
    ) {
        GrnDetailResponseDto updated = grnService.updateGrn(dto);
        return APIResponseBuilder.updated(updated, updated.getId());
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<APISuccessResponse<GrnDetailResponseDto>> complete(@PathVariable Integer id) {
        GrnDetailResponseDto completed = grnService.completeGrn(id);
        return APIResponseBuilder.ok(completed);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<APISuccessResponse<GrnDetailResponseDto>> cancel(@PathVariable Integer id) {
        GrnDetailResponseDto cancelled = grnService.cancelGrn(id);
        return APIResponseBuilder.ok(cancelled);
    }

}
