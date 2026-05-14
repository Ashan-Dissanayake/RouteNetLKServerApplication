package lk.ashan.routenetlkserverapllication.module.tripexecution.controller;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionInitializationDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionSummaryDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/trip-execution")
@RequiredArgsConstructor
public class TripExecutionController {

    private final TripExecutionService tripExecutionService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripExecutionDetailsResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<TripExecutionDetailsResponseDto> tripExecutions = params.isEmpty()
                ? tripExecutionService.getTripExecutions()
                : tripExecutionService.searchTripExecutions(params);

        return APIResponseBuilder.list(tripExecutions, tripExecutions.size());
    }

    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripExecutionSummaryDto>>> get() {
        List<TripExecutionSummaryDto> tripExecutions =  tripExecutionService.getSummaryTripExecution();
        return APIResponseBuilder.list(tripExecutions, tripExecutions.size());
    }

    @PostMapping("/initialize")
    public ResponseEntity<APISuccessResponse<List<TripExecutionDetailsResponseDto>>> createTrip(
            @RequestBody TripExecutionInitializationDto tripExecutionInitializationDto
    ){
      List<TripExecutionDetailsResponseDto> initializedTripExecutions =  tripExecutionService.
              initializeDailyExecutions(tripExecutionInitializationDto);
        return APIResponseBuilder.created(initializedTripExecutions,initializedTripExecutions.size());
    }

    @PostMapping("/generate-assignments")
    public ResponseEntity<Map<String, String>> generateAssignments(
            @RequestBody TripExecutionAssignmentDto tripExecutionAssignmentDto
    ) {
        tripExecutionService.generateTripExecutionAssignments(tripExecutionAssignmentDto);
        return ResponseEntity.ok(Map.of(
                "message", "Optimization completed successfully for " + tripExecutionAssignmentDto.getDate(),
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/{tripExecutionId}/checked-in")
    public ResponseEntity<Map<String, String>> checkedInTripExecution(
            @PathVariable Integer tripExecutionId
    ) {
        tripExecutionService.checkedInTripExecution(tripExecutionId);
        return ResponseEntity.ok(Map.of(
                "message", "Checked-In for " + tripExecutionId,
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/{tripExecutionId}/dispatched")
    public ResponseEntity<Map<String, String>> dispatchedTripExecution(
            @PathVariable Integer tripExecutionId
    ) {
        tripExecutionService.dispatchedTripExecution(tripExecutionId);
        return ResponseEntity.ok(Map.of(
                "message", "Dispatched for " + tripExecutionId,
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/{tripExecutionId}/arrived")
    public ResponseEntity<Map<String, String>> arrivedTripExecution(
            @PathVariable Integer tripExecutionId
    ) {
        tripExecutionService.arrivedTripExecution(tripExecutionId);
        return ResponseEntity.ok(Map.of(
                "message", "Arrived for " + tripExecutionId,
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/{tripExecutionId}/breakdown")
    public ResponseEntity<Map<String, String>> breakDownTripExecution(
            @PathVariable Integer tripExecutionId
    ) {
        tripExecutionService.breakdownTripExecution(tripExecutionId);
        return ResponseEntity.ok(Map.of(
                "message", "Breakdown for " + tripExecutionId,
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/{tripExecutionId}/completed")
    public ResponseEntity<Map<String, String>> completedTripExecution(
            @PathVariable Integer tripExecutionId
    ) {
        tripExecutionService.completedTripExecution(tripExecutionId);
        return ResponseEntity.ok(Map.of(
                "message", "Completed for " + tripExecutionId,
                "status", "SUCCESS"
        ));
    }

    @PostMapping("/{tripExecutionId}/cancelled")
    public ResponseEntity<Map<String, String>> cancelledTripExecution(
            @PathVariable Integer tripExecutionId
    ) {
        tripExecutionService.cancelledTripExecution(tripExecutionId);
        return ResponseEntity.ok(Map.of(
                "message", "Cancelled for " + tripExecutionId,
                "status", "SUCCESS"
        ));
    }
}
