package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryRequestDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TripUpdateRequestDto {
    @NotNull(message = "ID is mandatory for updates")
    private Integer id;

    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;

    @NotNull(message = "Trip type is mandatory")
    private TripTypeDto triptype;

    @NotNull(message = "Permit is mandatory")
    private PermitSummaryRequestDto permite;

    @NotNull(message = "Service date is mandatory")
    // Note: @FutureOrPresent is removed to allow editing historical/completed trips
    private LocalDate doservice;

    @NotNull(message = "Departure time is mandatory")
    private LocalTime todepature;

    @NotNull(message = "Arrival time is mandatory")
    private LocalTime toarrival;

    private String remarks;
    private Integer notrip;
    private TripStatusDto tripstatus;

    @NotNull(message = "Origin terminal is mandatory")
    private OriginTerminalDto originterminal;
}
