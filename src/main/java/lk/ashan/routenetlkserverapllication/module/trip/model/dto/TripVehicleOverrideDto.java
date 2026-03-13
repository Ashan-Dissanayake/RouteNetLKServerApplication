package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripVehicleOverrideDto{
    private Integer id;
    private String reason;
    private LocalDate dooverride;
    private VehicleSummaryResponseDto vehicle;
    private OverrideStatusDto overridestatus;
}
