package lk.ashan.routenetlkserverapllication.module.permit.controller;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.RouteSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.ServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.service.RouteService;
import lk.ashan.routenetlkserverapllication.module.permit.service.ServiceTypeService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/routes")
@RequiredArgsConstructor
public class RouteControllerController {

    private final RouteService routeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RouteSummaryResponseDto>>> get() {
        List<RouteSummaryResponseDto> routes = routeService.getRoutes();
        return APIResponseBuilder.list(routes, routes.size());
    }

}
