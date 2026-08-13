package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.ShiftSummaryDto;
import lombok.*;

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

    @NotNull(message = "Trip type is mandatory")
    private TripTypeDto triptype;

    @NotNull(message = "Permit is mandatory")
    private PermitSummaryRequestDto permite;

    @NotNull(message = "Departure time is mandatory")
    private LocalTime todepature;

    @NotNull(message = "Arrival time is mandatory")
    private LocalTime toarrival;

    private String remarks;

    private TripStatusDto tripstatus;

    @NotNull(message = "Shift is Mandatory")
    private ShiftSummaryDto shift;

    @NotNull(message = "Origin terminal is mandatory")
    private OriginTerminalDto originterminal;

    @NotNull(message = "OP Calender is mandatory")
    private OpCalenderSummaryDto opcalender;
}
