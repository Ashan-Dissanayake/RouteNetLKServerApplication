package lk.ashan.routenetlkserverapllication.report.controller;

import lk.ashan.routenetlkserverapllication.report.model.dto.ChartDataDTO;
import lk.ashan.routenetlkserverapllication.report.service.AnalyticsService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/dispatch-summary")
    public ResponseEntity<APISuccessResponse<ChartDataDTO>> getDispatchSummaryReport() {
        ChartDataDTO data = analyticsService.getReport1Metrics();
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/depot-revenue")
    public ResponseEntity<APISuccessResponse<ChartDataDTO>> getDepotRevenueReport() {
        ChartDataDTO data = analyticsService.getReport2Metrics();
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/maintenance-trends")
    public ResponseEntity<APISuccessResponse<ChartDataDTO>> getMaintenanceTrendsReport() {
        ChartDataDTO data = analyticsService.getReport3Metrics();
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/fleet-performance")
    public ResponseEntity<APISuccessResponse<ChartDataDTO>> getFleetPerformanceReport(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        ChartDataDTO data = analyticsService.getReport4Metrics(startDate, endDate);
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/incident-distribution")
    public ResponseEntity<APISuccessResponse<ChartDataDTO>> getIncidentDistributionReport() {
        ChartDataDTO data = analyticsService.getReport5Metrics();
        return APIResponseBuilder.ok(data);
    }
}
