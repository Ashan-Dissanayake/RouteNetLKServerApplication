package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripSummaryResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TripCrewAttendanceCreateRequestDto {
    @NotNull(message = "Trip is mandatory")
    private TripSummaryResponseDto trip;
    @NotNull(message = "Role is mandatory")
    private RoleDto role;
    @NotNull(message = "Planned employee is mandatory")
    private EmployeeSummaryDto plannedemployee;
}
