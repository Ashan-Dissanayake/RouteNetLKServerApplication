package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ISSUE #15: DTO for trip search requests
 * Replaces HashMap<String, String> with typed request object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripSearchRequest {
    
    private Integer tripTypeId;
    private LocalTime departureTime;
    private Integer tripStatusId;
    private LocalDate serviceDate;
    private LocalDate serviceDateFrom;
    private LocalDate serviceDateTo;
    private Integer branchId;
    private Integer permitId;
    private Integer routeId;
    private Integer originTerminalId;
    private String statusName;
    private Boolean activeOnly;  // Filter for active trips (not cancelled/completed)
}
