package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for representing detailed information about a trip.
 * This class includes details such as the trip's ID, associated branch, trip type,
 * permit, break minutes, departure and arrival times, remarks, origin terminal,
 * trip status, and operational calendar.
 *
 * <p>It uses Lombok annotations to reduce boilerplate code for getters, setters,
 * constructors, builders, and the toString method.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripDetailResponseDto {
    private Integer id;
    private BranchSummaryDto branch;
    private TripTypeDto triptype;
    private PermitSummaryResponseDto permite;
    private Integer breakminutes;
    private LocalTime todepature;
    private LocalTime toarrival;
    private String remarks;
    private OriginTerminalDto originterminal;
    private TripStatusDto tripstatus;
    private OpCalenderSummaryDto opcalender;
}
