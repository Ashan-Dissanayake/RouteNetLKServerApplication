package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TripCrewAttendanceUpdateRequestDto {
    @NotNull(message = "Check in time is mandatory")
    private LocalTime tocheckin;
    private LocalTime tocheckout;
    private EmployeeSummaryDto actualemployee;
    private CrewAttendanceStatusDto crewattendancestatus;
}
