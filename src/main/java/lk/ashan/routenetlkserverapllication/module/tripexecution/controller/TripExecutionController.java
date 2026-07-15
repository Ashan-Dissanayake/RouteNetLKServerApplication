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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('trip-execution-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripExecutionDetailsResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<TripExecutionDetailsResponseDto> tripExecutions = params.isEmpty()
                ? tripExecutionService.getTripExecutions()
                : tripExecutionService.searchTripExecutions(params);

        return APIResponseBuilder.list(tripExecutions, tripExecutions.size());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripExecutionSummaryDto>>> get() {
        List<TripExecutionSummaryDto> tripExecutions =  tripExecutionService.getSummaryTripExecution();
        return APIResponseBuilder.list(tripExecutions, tripExecutions.size());
    }

    @PreAuthorize("hasAuthority('trip-execution-initialize')")
    @PostMapping("/initialize")
    public ResponseEntity<APISuccessResponse<List<TripExecutionDetailsResponseDto>>> createTrip(
            @RequestBody TripExecutionInitializationDto tripExecutionInitializationDto
    ){
      List<TripExecutionDetailsResponseDto> initializedTripExecutions =  tripExecutionService.
              initializeDailyExecutions(tripExecutionInitializationDto);
        return APIResponseBuilder.created(initializedTripExecutions,initializedTripExecutions.size());
    }

    @PreAuthorize("hasAuthority('trip-execution-generate-assignments')")
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

    @PreAuthorize("hasAuthority('trip-execution-checked-in')")
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

    @PreAuthorize("hasAuthority('trip-execution-dispatched')")
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

    @PreAuthorize("hasAuthority('trip-execution-arrived')")
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

    @PreAuthorize("hasAuthority('trip-execution-breakdown')")
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

    @PreAuthorize("hasAuthority('trip-execution-completed')")
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

    @PreAuthorize("hasAuthority('trip-execution-cancelled')")
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
