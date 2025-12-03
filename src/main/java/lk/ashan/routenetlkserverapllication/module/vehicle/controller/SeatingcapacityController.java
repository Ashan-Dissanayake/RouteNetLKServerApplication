package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.SeatingcapacityDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.SeatingcapacityService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/seatingcapacities")
@RequiredArgsConstructor
public class SeatingcapacityController {

    private final SeatingcapacityService seatingcapacityService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<SeatingcapacityDto>>> get() {
        List<SeatingcapacityDto> seatingcapacities = seatingcapacityService.getSeatingcapacities();
        return APIResponseBuilder.getResponse(seatingcapacities, seatingcapacities.size());
    }

}
