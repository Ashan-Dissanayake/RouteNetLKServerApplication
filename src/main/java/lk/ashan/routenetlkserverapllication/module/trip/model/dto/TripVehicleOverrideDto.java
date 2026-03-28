package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
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
    private VehicleSummaryDto vehicle;
    private OverrideStatusDto overridestatus;
}
