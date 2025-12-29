package lk.ashan.routenetlkserverapllication.module.crew.controller;

import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.ConductorService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/conductors")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ConductorDetailResponseDto>>> get() {
        List<ConductorDetailResponseDto> conductors = conductorService.getConductors();
        return APIResponseBuilder.getResponse(conductors, conductors.size());
    }

}
