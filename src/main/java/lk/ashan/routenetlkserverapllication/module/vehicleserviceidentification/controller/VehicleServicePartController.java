package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.controller;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartBulkCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartBulkUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service.VehicleServicePartService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/vehicleserviceparts")
@RequiredArgsConstructor
@CrossOrigin
public class VehicleServicePartController {

    private final VehicleServicePartService vehicleServicePartService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServicePartDetailResponseDto>>> createParts(
            @RequestBody @Valid VehicleServicePartBulkCreateRequestDto dto) {

        List<VehicleServicePartDetailResponseDto> parts =
                vehicleServicePartService.createParts(dto);

        return APIResponseBuilder.created(parts,parts.size());
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServicePartDetailResponseDto>>> updateParts(
            @RequestBody @Valid VehicleServicePartBulkUpdateRequestDto dto) {

        List<VehicleServicePartDetailResponseDto> parts =
                vehicleServicePartService.updateParts(dto);

        return APIResponseBuilder.created(parts,parts.size());
    }



}
