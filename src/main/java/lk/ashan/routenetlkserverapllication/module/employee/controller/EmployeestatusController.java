package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeestatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeestatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/employeestatuses")
@RequiredArgsConstructor
public class EmployeestatusController {

    private final EmployeestatusService employeestatusService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeestatusDto>>> get() {
        List<EmployeestatusDto> employeestatuses = employeestatusService.getEmployeestatuses();
        return APIResponseBuilder.getResponse(employeestatuses, employeestatuses.size());
    }

}
