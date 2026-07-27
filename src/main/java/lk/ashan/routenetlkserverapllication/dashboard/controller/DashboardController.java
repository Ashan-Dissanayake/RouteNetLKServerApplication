package lk.ashan.routenetlkserverapllication.dashboard.controller;

import lk.ashan.routenetlkserverapllication.dashboard.dto.DashboardOverviewDto;
import lk.ashan.routenetlkserverapllication.dashboard.service.DashboardService;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.security.CustomUserPrincipal;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

//    @GetMapping("/overview")
//    public ResponseEntity<APISuccessResponse<DashboardOverviewDto>> getDepotDashboardOverview(
//            @AuthenticationPrincipal CustomUserPrincipal principal) // 1. Catch the wrapper type cleanly
//    {
//        // Safety check: Ensure the security context principal exists
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        // 2. Extract your rich database entity directly from the custom wrapper principal
//        User loggedInUser = principal.getUserEntity();
//
//        // 3. Extract the operational branch ID via the authenticated user's profile
//        // Safe check: Ensure user and their employee/branch associations are present
//        if (loggedInUser.getEmployee() == null || loggedInUser.getEmployee().getBranch() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Integer branchId = loggedInUser.getEmployee().getBranch().getId();
//
//        // 4. Fetch the aggregated dashboard metrics for this branch
//        DashboardOverviewDto overviewData = dashboardService.getDepotDashboardOverviewDummy(branchId);
//
//        // 5. Return a clean HTTP 200 OK using the unified API response wrapper
//        return APIResponseBuilder.ok(overviewData);
//    }

    @GetMapping("/overview")
    public ResponseEntity<APISuccessResponse<DashboardOverviewDto>> getDepotDashboardOverview(
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User loggedInUser = principal.getUserEntity();
        if (loggedInUser.getEmployee() == null || loggedInUser.getEmployee().getBranch() == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer branchId = loggedInUser.getEmployee().getBranch().getId();

        // Dummy data
        DashboardOverviewDto overviewData = dashboardService.getDepotDashboardOverviewDummy(branchId);

        // Wrap inside API response envelope
        return APIResponseBuilder.ok(overviewData);
    }

}
