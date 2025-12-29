package lk.ashan.routenetlkserverapllication.module.crew.dto;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
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
    private EmployeeSummaryResponseDto employee;
}
