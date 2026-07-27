package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.trip.validation.annotation.ValidTimeRange;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Data Transfer Object (DTO) for updating trip details.
 * This class is used to encapsulate the data required for updating a trip.
 * It includes fields such as trip ID, branch, trip type, permit, departure and arrival times, remarks, status, shift, and origin terminal.
 * Validation annotations are used to ensure mandatory fields are provided.
 */
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

    @NotNull(message = "Departure time is mandatory")
    private LocalTime todepature;

    @NotNull(message = "Arrival time is mandatory")
    private LocalTime toarrival;

    private String remarks;

    @NotNull(message = "Status is mandatory")
    private TripStatusDto tripstatus;

    @NotNull(message = "Shift is Mandatory")
    private Shift shift;

    @NotNull(message = "Origin terminal is mandatory")
    private OriginTerminalDto originterminal;
}
