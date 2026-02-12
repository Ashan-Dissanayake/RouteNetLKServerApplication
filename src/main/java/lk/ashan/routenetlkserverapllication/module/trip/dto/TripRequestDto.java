package lk.ashan.routenetlkserverapllication.module.trip.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.validation.annotation.ValidTimeRange;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class TripRequestDto {
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryResponseDto branch;
    @NotNull(message = "Trip type is mandatory")
    private TripTypeDto triptype;
    @NotNull(message = "Permit is mandatory")
    private PermitSummaryRequestDto permite;
    @NotNull(message = "Service date is mandatory")
    @FutureOrPresent(message = "Service date cannot be in the past")
    private LocalDate doservice;
    @NotNull(message = "Departure time is mandatory")
    private LocalTime todepature;
    @NotNull(message = "Arrival time is mandatory")
    private LocalTime toarrival;
    private String remarks;
    @NotBlank(message = "Number is mandatory")
    private Integer notrip;
    @NotNull(message = "Trip status is mandatory")
    private TripStatusDto tripstatus;
    @NotNull(message = "Origin terminal is mandatory")
    private OriginTerminalDto originterminal;
}
