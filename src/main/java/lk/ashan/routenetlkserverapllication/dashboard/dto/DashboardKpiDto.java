package lk.ashan.routenetlkserverapllication.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKpiDto {
    private long activeTrips;
    private long pendingIncidents;
    private long standbyDrivers;
    private long standbyConductors;
}
