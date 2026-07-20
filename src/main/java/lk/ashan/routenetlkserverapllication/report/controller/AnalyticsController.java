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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/dispatch-summary")
    public ResponseEntity<APISuccessResponse<Report1ResponseDto>> getDispatchSummaryReport() {
        Report1ResponseDto data = analyticsService.getFleetDispatchAndBreakdownMetrics();
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/depot-revenue")
    public ResponseEntity<APISuccessResponse<Report2ResponseDto>> getDepotRevenueReport() {
        Report2ResponseDto data = analyticsService.getDepotRevenueMetrics();
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/maintenance-trends")
    public ResponseEntity<APISuccessResponse<Report3ResponseDto>> getMaintenanceTrendsReport() {
        Report3ResponseDto data = analyticsService.getMaintenanceTrendsMetrics();
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/fleet-performance")
    public ResponseEntity<APISuccessResponse<Report4ResponseDto>> getFleetPerformanceReport(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Report4ResponseDto data = analyticsService.getDynamicPerformanceMetrics(startDate, endDate);
        return APIResponseBuilder.ok(data);
    }

    @PreAuthorize("hasAuthority('report-view')")
    @GetMapping("/incident-distribution")
    public ResponseEntity<APISuccessResponse<Report5ResponseDto>> getIncidentDistributionReport() {
        Report5ResponseDto data = analyticsService.getIncidentDistributionMetrics();
        return APIResponseBuilder.ok(data);
    }

}
