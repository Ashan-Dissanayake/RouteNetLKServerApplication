package lk.ashan.routenetlkserverapllication.module.trip.controller;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OriginTerminalDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.OriginTerminalService;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripTypeService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/origin-terminals")
@RequiredArgsConstructor
public class OriginTerminalController {

    private final OriginTerminalService originTerminalService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<OriginTerminalDto>>> get() {
        List<OriginTerminalDto> originTerminals = originTerminalService.getOriginTerminals();
        return APIResponseBuilder.list(originTerminals, originTerminals.size());
    }

}
