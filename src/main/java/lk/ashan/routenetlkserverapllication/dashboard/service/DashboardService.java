package lk.ashan.routenetlkserverapllication.dashboard.service;

import io.micrometer.common.lang.Nullable;
import lk.ashan.routenetlkserverapllication.dashboard.dto.*;
import lk.ashan.routenetlkserverapllication.dashboard.mapper.DashboardMapper;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.farecollection.repository.FareCollectionRepository;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftRepository;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TripExecutionRepository tripExecutionRepository;
    private final IncidentRepository incidentRepository;
    private final DriverRepository driverRepository;
    private final ConductorRepository conductorRepository;
    private final FareCollectionRepository fareCollectionRepository;
    private final RosterShiftRepository rosterShiftRepository;
    private final RosterShiftAssignmentRepository rosterShiftAssignmentRepository;
    private final DashboardMapper dashboardMapper;

    @Transactional(readOnly = true)
    public DashboardOverviewDto getDepotDashboardOverview(Integer branchId) {

        // 1. Fetch KPI Metrics
        DashboardKpiDto kpis = DashboardKpiDto.builder()
                .activeTrips(tripExecutionRepository.countActiveTripsByBranch(branchId))
                .pendingIncidents(incidentRepository.countPendingIncidentsByBranch(branchId))
                .standbyDrivers(driverRepository.countStandbyDriversByBranch(branchId))
                .standbyConductors(conductorRepository.countStandbyConductorsByBranch(branchId))
                .build();

        // 2. Fetch and Extract Daily Financial Aggregate Summaries
        Object[] rawRevenue = fareCollectionRepository.getDailyRevenueSummaryByBranch(branchId);
        DepotRevenueDto revenue = mapToRevenueDTO(rawRevenue);

        // 3. Fetch Active Fleet Incidents (Transformed via MapStruct)
        List<Incident> activeIncidents = incidentRepository.findActiveIncidentsByBranch(branchId);
        List<ActiveIncidentDto> incidentDTOs = dashboardMapper.toActiveIncidentDTOList(activeIncidents);

        // 4. Fetch and Process Today's Roster Shift Allocations
        List<RosterShift> todayShifts = rosterShiftRepository.findTodayShiftsByBranchNative(branchId);
        List<ShiftCoverageDto> shiftCoverageDTOs = processShiftCoverage(todayShifts);

        // 5. Package and Assemble Root Response Component
        return DashboardOverviewDto.builder()
                .kpis(kpis)
                .revenue(revenue)
                .activeIncidents(incidentDTOs)
                .todayShiftCoverage(shiftCoverageDTOs)
                .build();
    }

    /**
     * Extracts numerical object arrays from native aggregation queries into structured DTO models.
     */
    private DepotRevenueDto mapToRevenueDTO(@Nullable Object[] result) {
        if (result == null || result.length == 0 || result[0] == null) {
            return DepotRevenueDto.builder()
                    .totalTickets(0L)
                    .cashCollected(BigDecimal.ZERO)
                    .digitalPayments(BigDecimal.ZERO)
                    .isReconciled(false)
                    .build();
        }

        Object[] row = (Object[]) result[0];
        return DepotRevenueDto.builder()
                .totalTickets(((Number) row[0]).longValue())
                .cashCollected(new BigDecimal(row[1].toString()))
                .digitalPayments(new BigDecimal(row[2].toString()))
                .isReconciled(false) // Default initial dashboard status before reconciliation verification
                .build();
    }

    private List<ShiftCoverageDto> processShiftCoverage(List<RosterShift> shifts) {
        List<ShiftCoverageDto> coverageList = new ArrayList<>();

        for (RosterShift shift : shifts) {
            // Safe extraction of planned required metrics
            int required = shift.getRequiredemployeecount() != null ? shift.getRequiredemployeecount() : 0;

            // Fixed: Fetch the real-time headcount dynamically via repository query
            int assigned = rosterShiftAssignmentRepository.countAssignmentsByRosterShiftId(shift.getId());

            // Evaluate fulfillment condition rule
            String status = (assigned >= required && required > 0) ? "FULLY_STAFFED" : "UNDERSTAFFED";

            // Build timing strings from the shift association structure[cite: 2]
            String timeRange = "Flexible Hours";
            if (shift.getShift() != null) {
                timeRange = shift.getShift().getTostart() + " - " + shift.getShift().getToend();
            }

            coverageList.add(ShiftCoverageDto.builder()
                    .shiftName(shift.getShift() != null ? shift.getShift().getName() : "Standard Shift")
                    .timeRange(timeRange)
                    .requiredCount(required)
                    .assignedCount(assigned)
                    .status(status)
                    .build());
        }

        return coverageList;
    }

}
