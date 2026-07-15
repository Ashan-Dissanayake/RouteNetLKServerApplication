package lk.ashan.routenetlkserverapllication.module.farecollection.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.service.FareCollectionService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/fare-collections")
@RequiredArgsConstructor
public class FareCollectionController {

    private final FareCollectionService fareCollectionService;

    @PreAuthorize("hasAuthority('fare-collection-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<FareCollectionDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<FareCollectionDetailResponseDto> fareCollections = params.isEmpty()
                ? fareCollectionService.getFareCollections()
                : fareCollectionService.searchFareCollections(params);

        return APIResponseBuilder.list(fareCollections, fareCollections.size());
    }

    @PreAuthorize("hasAuthority('fare-collection-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<FareCollectionDetailResponseDto>> add(
            @RequestBody @Valid FareCollectionCreateRequestDto createRequestDto
    ) {
        FareCollectionDetailResponseDto savedFareCollection = fareCollectionService.createFareCollection(createRequestDto);
        return APIResponseBuilder.created(savedFareCollection, savedFareCollection.getId());
    }

    @PreAuthorize("hasAuthority('fare-collection-reconcile')")
    @PostMapping("/{fareCollectionId}/reconciled")
    public ResponseEntity<Map<String, String>> reconciled(
            @PathVariable Integer fareCollectionId
    ) {
        fareCollectionService.reconciled(fareCollectionId);
        return ResponseEntity.ok(Map.of(
                "message", "Reconciled for " + fareCollectionId,
                "status", "SUCCESS"
        ));
    }
}
