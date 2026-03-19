package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.service.TripCrewAttendanceService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crew-attendances")
@RequiredArgsConstructor
public class TripCrewAttendanceController {

    private final TripCrewAttendanceService attendanceService;

    @PutMapping
    public ResponseEntity<APISuccessResponse<TripCrewAttendanceDetailsResponseDto>> update(
            @RequestBody @Valid TripCrewAttendanceUpdateRequestDto requestDto
    ) {
       TripCrewAttendanceDetailsResponseDto updatedAttendance = attendanceService.updateCrewAttendance(requestDto);
        return APIResponseBuilder.updated(updatedAttendance,updatedAttendance.getId());
    }

}
