package lk.ashan.routenetlkserverapllication.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewDto {
    private String depotName;
    private DashboardKpiDto kpis;
    private DepotRevenueDto revenue;
    private List<ActiveIncidentDto> activeIncidents;
    private List<ShiftCoverageDto> todayShiftCoverage;
}
