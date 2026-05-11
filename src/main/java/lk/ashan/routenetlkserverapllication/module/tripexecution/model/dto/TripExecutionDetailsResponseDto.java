package lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto;


import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TripExecutionDetailsResponseDto {
    private BranchSummaryDto branch;
    private Integer id;
    private LocalDate doservice;
    private String status;

    private Integer tripId;
    private String routeName;
    private LocalTime plannedDeparture;
    private LocalTime plannedArrival;
    private String tripType;

    private Integer startodometer;
    private Integer endodometer;
    private Integer passengercount;

    // Resource Indicators (Will be null/empty initially)
    private String vehicleNumber;
    private String driverName;
    private String conductorName;

}
