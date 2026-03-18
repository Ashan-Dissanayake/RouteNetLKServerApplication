package lk.ashan.routenetlkserverapllication.module.crew.model.dto;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ConductorDetailResponseDto {
    private Integer id;
    private String number;
    private LocalDate domedicalissued;
    private LocalDate domedicalexpired;
    private CrewStatusDto crewstatus;
    private RouteFamiliarityLevelDto routefamiliaritylevel;
    private EmployeeSummaryDto employee;
}
