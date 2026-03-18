package lk.ashan.routenetlkserverapllication.module.crew.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class ConductorRequestDto {
    @NotNull(message = "Number can not be empty")
    @Pattern(regexp = "^CON-\\d{4}-\\d{3}$",message = "Invalid conductor number")
    private String number;

    @NotNull(message = "Medical issued date is mandatory")
    @PastOrPresent(message = "Medical issued date is cannot be in the future")
    private LocalDate domedicalissued;

    @NotNull(message = "Medical expired date is mandatory")
    @Future(message = "Medical expired date cannot be in the past or present")
    private LocalDate domedicalexpired;

    @NotNull(message = "Crew status is mandatory")
    private CrewStatusDto crewstatus;

    @NotNull(message = "Route Familiarity Level is mandatory")
    private RouteFamiliarityLevelDto routefamiliaritylevel;

    @NotNull(message = "Employee is mandatory")
    private EmployeeSummaryDto employee;
}
