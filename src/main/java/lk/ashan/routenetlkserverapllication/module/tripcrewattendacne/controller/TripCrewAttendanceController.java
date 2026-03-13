package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.controller;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.CrewCheckInRequestDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.service.TripCrewAttendanceService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crew-attendances")
@RequiredArgsConstructor
public class TripCrewAttendanceController {

    private final TripCrewAttendanceService attendanceService;

    @PutMapping("/{attendanceId}/checkin")
    public ResponseEntity<APISuccessResponse<String>> checkIn(
            @RequestBody CrewCheckInRequestDto requestDto
    ) {
        attendanceService.checkIn(requestDto);
        return APIResponseBuilder.ok("Check-in completed successfully");
    }

    @PutMapping("/{attendanceId}/absent")
    public ResponseEntity<APISuccessResponse<String>> markAbsent(
            @PathVariable Integer attendanceId
    ) {

        attendanceService.markAbsent(attendanceId);

        return APIResponseBuilder.ok("Marked as absent successfully");
    }

}
