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

/**
 * Controller for handling GRN (Goods Received Note) related operations.
 * Provides endpoints for viewing and updating GRNs.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/grns")
@RequiredArgsConstructor
public class GrnController {

    private final GrnService grnService;

    /**
     * Retrieves a list of GRNs. If query parameters are provided, it performs a search
     * based on the given parameters.
     *
     * @param params A map of query parameters for filtering GRNs.
     * @return A ResponseEntity containing a list of GRN details wrapped in an API success response.
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the 'grn-view' authority.
     */
    @PreAuthorize("hasAuthority('grn-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<GrnDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<GrnDetailResponseDto> requests = params.isEmpty()
                ? grnService.getGrns()
                : grnService.searchGrns(params);

        return APIResponseBuilder.list(requests, requests.size());
    }

    /**
     * Updates an existing GRN with the provided details.
     *
     * @param dto The GRN update request data transfer object containing the updated details.
     * @return A ResponseEntity containing the updated GRN details wrapped in an API success response.
     * @throws jakarta.validation.ConstraintViolationException if the provided data is invalid.
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the 'grn-update' authority.
     */
    @PreAuthorize("hasAuthority('grn-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<GrnDetailResponseDto>> update(
            @RequestBody @Valid GrnUpdateRequestDto dto
    ) {
        GrnDetailResponseDto updated = grnService.updateGrn(dto);
        return APIResponseBuilder.updated(updated, updated.getId());
    }

}
