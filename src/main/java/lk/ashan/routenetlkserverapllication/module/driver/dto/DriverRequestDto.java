package lk.ashan.routenetlkserverapllication.module.driver.dto;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.shared.validation.driver.licensecategorylicensenumber.ValidLicenseCategoryLicenseNumber;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@ValidLicenseCategoryLicenseNumber
public class DriverRequestDto {
    @NotNull(message = "Number can not be empty")
    @Pattern(regexp = "^DRV-\\d{4}-\\d{3}$")
    private String number;
    @NotNull(message = "License number can not be empty")
    private String licensenumber;
    private LocalDate dolicenseexpired;
    private LocalDate domedicalexpired;
    private LicenseCategoryDto licensecategory;
    private CrewStatusDto crewstatus;
    private RouteFamiliarityLevelDto routefamiliaritylevel;
    private AllowedBusTypeDto allowedbustype;
    private EmployeeSummaryResponseDto employee;
}
