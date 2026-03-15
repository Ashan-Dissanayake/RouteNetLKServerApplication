package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lombok.*;


import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCrewAttendanceDetailsResponseDto {
    private Integer id;
    private TripSummaryResponseDto trip;
    private RoleDto role;
    private LocalTime tocheckin;
    private LocalTime tocheckout;
    private EmployeeSummaryResponseDto plannedemployee;
    private EmployeeSummaryResponseDto actualemployee;
    private CrewAttendanceStatusDto crewattendancestatus;
}
