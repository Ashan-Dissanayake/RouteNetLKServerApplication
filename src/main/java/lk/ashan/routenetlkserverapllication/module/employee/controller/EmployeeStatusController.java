package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeStatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeeStatusService;
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
@RequestMapping(value = "/employee-statuses")
@RequiredArgsConstructor
public class EmployeeStatusController {

    private final EmployeeStatusService employeestatusService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeeStatusDto>>> get() {
        List<EmployeeStatusDto> employeeStatuses = employeestatusService.getEmployeeStatuses();
        return APIResponseBuilder.list(employeeStatuses, employeeStatuses.size());
    }

}
