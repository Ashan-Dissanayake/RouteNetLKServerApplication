package lk.ashan.routenetlkserverapllication.module.permit.model.dto;

import jakarta.validation.constraints.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermitCreateRequestDto{
    @NotBlank(message = "Number is mandatory")
    @Pattern(regexp = "^(?:[AF]\\d{5}|\\d{4})$",message = "Invalid Permit Number")
    private String number;
    @NotNull(message = "Vehicle is mandatory")
    private VehicleSummaryDto vehicle;
    @NotNull(message = "Date of issued is mandatory")
    @PastOrPresent(message = "Issued date cannot be in the future")
    private LocalDate doissued;

    @NotNull(message = "Trip count is mandatory")
    @Positive(message = "Negative values are not allowed")
    private Integer notripsperday;

    @NotNull(message = "Permit status is mandatory")
    private PermitStatusDto permitestatus;
    @NotNull(message = "Service type is mandatory")
    private ServiceTypeDto servicetype;
    @NotNull(message = "Route is mandatory")
    private RouteSummaryRequestDto route;
}
