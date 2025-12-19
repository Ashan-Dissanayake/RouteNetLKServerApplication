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
public class DriverDetailResponseDto {
    private Integer id;
    private String number;
    private String licensenumber;
    private LocalDate dolicenseexpired;
    private LocalDate domedicalexpired;
    private LicenseCategoryDto licensecategory;
    private CrewStatusDto crewstatus;
    private RouteFamiliarityLevelDto routefamiliaritylevel;
    private AllowedBusTypeDto allowedbustype;
    private EmployeeSummaryResponseDto employee;
}
