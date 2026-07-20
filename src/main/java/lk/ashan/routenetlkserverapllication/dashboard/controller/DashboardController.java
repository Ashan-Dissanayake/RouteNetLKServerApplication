package lk.ashan.routenetlkserverapllication.dashboard.controller;

import lk.ashan.routenetlkserverapllication.dashboard.dto.DashboardOverviewDto;
import lk.ashan.routenetlkserverapllication.dashboard.service.DashboardService;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<APISuccessResponse<DashboardOverviewDto>> getDepotDashboardOverview(
            @AuthenticationPrincipal User loggedInUser) {

        // 1. Extract the operational branch ID via the authenticated user's profile
        // Safe check: Ensure user and their employee association are present
        if (loggedInUser == null || loggedInUser.getEmployee() == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer branchId = loggedInUser.getEmployee().getBranch().getId();

        // 2. Fetch the aggregated dashboard metrics for this branch
        DashboardOverviewDto overviewData = dashboardService.getDepotDashboardOverview(branchId);

        // 3. Return a clean HTTP 200 OK using the unified API response wrapper
        return APIResponseBuilder.ok(overviewData);
    }

}
