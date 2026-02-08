package lk.ashan.routenetlkserverapllication.module.permit.dto;

import jakarta.validation.constraints.*;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.shared.validation.RegexPattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class PermitRequestDto {
    @NotBlank(message = "Number is mandatory")
    @Pattern(regexp = "^(\\d{4}|[A-Z]{3}-[A-Z]{2}\\d+-\\d+(?:-\\d+)*(?:\\\\[A-Z]{3}-[A-Z]{2}\\d+(?:-\\d+)*)*)$",message = "Invalid Permit Number")
    private String number;
    @NotNull(message = "Vehicle is mandatory")
    private VehicleSummaryResponseDto vehicle;
    @NotNull(message = "Date of issued is mandatory")
    @PastOrPresent(message = "Issued date cannot be in the future")
    private LocalDate doissued;
    @NotNull(message = "Date of Exp is mandatory")
    @Future(message = "Exp date cannot be in the past or present")
    private LocalDate doexpired;
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryResponseDto branch;
    @NotNull(message = "Permit status is mandatory")
    private PermitStatusDto permitestatus;
    @NotNull(message = "Service type is mandatory")
    private ServiceTypeDto servicetype;
    @NotNull(message = "Route is mandatory")
    private RouteSummaryRequestDto route;
}
