package lk.ashan.routenetlkserverapllication.module.trip.dto;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
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
