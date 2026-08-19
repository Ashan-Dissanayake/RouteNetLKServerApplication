package lk.ashan.routenetlkserverapllication.report.controller;

import lk.ashan.routenetlkserverapllication.report.model.dto.*;
import lk.ashan.routenetlkserverapllication.report.service.AnalyticsService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Controller for handling analytics-related endpoints.
 * Provides various reports such as dispatch summary, depot revenue, maintenance trends, fleet performance, and incident distribution.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Retrieves the dispatch summary report.
     *
     * @return a ResponseEntity containing the dispatch summary report data.
     */
    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/dispatch-summary")
    public ResponseEntity<APISuccessResponse<Report1ResponseDto>> getDispatchSummaryReport() {
        Report1ResponseDto data = analyticsService.getFleetDispatchAndBreakdownMetrics();
        return APIResponseBuilder.ok(data);
    }

    /**
     * Retrieves the depot revenue report.
     *
     * @return a ResponseEntity containing the depot revenue report data.
     */
    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/depot-revenue")
    public ResponseEntity<APISuccessResponse<Report2ResponseDto>> getDepotRevenueReport() {
        Report2ResponseDto data = analyticsService.getDepotRevenueMetrics();
        return APIResponseBuilder.ok(data);
    }

    /**
     * Retrieves the maintenance trends report.
     *
     * @return a ResponseEntity containing the maintenance trends report data.
     */
    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/maintenance-trends")
    public ResponseEntity<APISuccessResponse<Report3ResponseDto>> getMaintenanceTrendsReport() {
        Report3ResponseDto data = analyticsService.getMaintenanceTrendsMetrics();
        return APIResponseBuilder.ok(data);
    }

    /**
     * Retrieves the fleet performance report for a given date range.
     *
     * @param startDate the start date of the report in yyyy-MM-dd format.
     * @param endDate the end date of the report in yyyy-MM-dd format.
     * @return a ResponseEntity containing the fleet performance report data.
     */
    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/fleet-performance")
    public ResponseEntity<APISuccessResponse<Report4ResponseDto>> getFleetPerformanceReport(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Report4ResponseDto data = analyticsService.getDynamicPerformanceMetrics(startDate, endDate);
        return APIResponseBuilder.ok(data);
    }

    /**
     * Retrieves the incident distribution report.
     *
     * @return a ResponseEntity containing the incident distribution report data.
     */
    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/incident-distribution")
    public ResponseEntity<APISuccessResponse<Report5ResponseDto>> getIncidentDistributionReport() {
        Report5ResponseDto data = analyticsService.getIncidentDistributionMetrics();
        return APIResponseBuilder.ok(data);
    }

}
