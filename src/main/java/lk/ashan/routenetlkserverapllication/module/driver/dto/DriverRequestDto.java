package lk.ashan.routenetlkserverapllication.module.driver.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.shared.validation.driver.licensecategorylicensenumber.ValidLicenseCategoryLicenseNumber;
import lombok.*;
import lombok.experimental.SuperBuilder;


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
    @Pattern(regexp = "^DRV-\\d{4}-\\d{3}$",message = "Invalid driver number")
    private String number;

    @NotNull(message = "License number can not be empty")
    private String licensenumber;

    @NotNull(message = "License issued date is mandatory")
    @PastOrPresent(message = "License issued date is cannot be in the future")
    private LocalDate dolicenseissued;

    @NotNull(message = "License expired date is mandatory")
    @Future(message = "License Expired date cannot be in the past or present")
    private LocalDate dolicenseexpired;

    @NotNull(message = "Medical issued date is mandatory")
    @PastOrPresent(message = "Medical issued date is cannot be in the future")
    private LocalDate domedicalissued;

    @NotNull(message = "Medical expired date is mandatory")
    @Future(message = "Medical expired date cannot be in the past or present")
    private LocalDate domedicalexpired;

    @NotNull(message = "License Category is mandatory")
    private LicenseCategoryDto licensecategory;

    @NotNull(message = "Crew status is mandatory")
    private CrewStatusDto crewstatus;

    @NotNull(message = "Route Familiarity Level is mandatory")
    private RouteFamiliarityLevelDto routefamiliaritylevel;

    @NotNull(message = "Employee is mandatory")
    private EmployeeSummaryResponseDto employee;
}
