package lk.ashan.routenetlkserverapllication.module.tripexecution.controller;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionStatusDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionStatusService;
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
@RequestMapping(value = "/trip-execution-statuses")
@RequiredArgsConstructor
public class TripExecutionStatusController {

    private final TripExecutionStatusService tripExecutionStatusService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripExecutionStatusDto>>> get() {
        List<TripExecutionStatusDto> tripExecutionStatuses = tripExecutionStatusService.getTripExecutionStatuses();
        return APIResponseBuilder.list(tripExecutionStatuses, tripExecutionStatuses.size());
    }

}
