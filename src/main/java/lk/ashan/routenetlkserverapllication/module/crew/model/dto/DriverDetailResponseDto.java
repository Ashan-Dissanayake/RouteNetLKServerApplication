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
public class DriverDetailResponseDto {
    private Integer id;
    private String number;
    private String licensenumber;
    private LocalDate dolicenseissued;
    private LocalDate dolicenseexpired;
    private LocalDate domedicalissued;
    private LocalDate domedicalexpired;
    private LicenseCategoryDto licensecategory;
    private CrewStatusDto crewstatus;
    private RouteFamiliarityLevelDto routefamiliaritylevel;
    private EmployeeSummaryDto employee;
}
