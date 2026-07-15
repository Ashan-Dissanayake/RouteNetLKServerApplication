package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryRequestDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for creating a new trip.
 * This class contains the necessary fields required to create a trip,
 * including branch, trip type, permit, departure and arrival times, and more.
 * It uses Lombok annotations for boilerplate code reduction.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TripCreateRequestDto {

    /**
     * The branch associated with the trip.
     * This field is mandatory.
     */
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;

    /**
     * The type of the trip.
     * This field is mandatory.
     */
    @NotNull(message = "Trip type is mandatory")
    private TripTypeDto triptype;

    /**
     * The permit associated with the trip.
     * This field is mandatory.
     */
    @NotNull(message = "Permit is mandatory")
    private PermitSummaryRequestDto permite;

    /**
     * The departure time of the trip.
     * This field is mandatory.
     */
    @NotNull(message = "Departure time is mandatory")
    private LocalTime todepature;

    /**
     * The arrival time of the trip.
     * This field is mandatory.
     */
    @NotNull(message = "Arrival time is mandatory")
    private LocalTime toarrival;

    /**
     * Additional remarks about the trip.
     * This field is optional.
     */
    private String remarks;

    /**
     * The status of the trip.
     * This field is optional.
     */
    private TripStatusDto tripstatus;

    /**
     * The origin terminal of the trip.
     * This field is mandatory.
     */
    @NotNull(message = "Origin terminal is mandatory")
    private OriginTerminalDto originterminal;

    /**
     * The operational calendar associated with the trip.
     * This field is mandatory.
     */
    @NotNull(message = "OP Calender is mandatory")
    private OpCalenderSummaryDto opcalender;
}
