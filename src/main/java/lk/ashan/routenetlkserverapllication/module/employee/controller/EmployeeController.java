package lk.ashan.routenetlkserverapllication.module.employee.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeeService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeeDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<EmployeeDetailResponseDto> employees = params.isEmpty()
                ?employeeService.getEmployees()
                : employeeService.searchEmployee(params);

        return APIResponseBuilder.list(employees, employees.size());
    }

    @GetMapping(value = "/list",produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeeSummaryResponseDto>>> get() {
        List<EmployeeSummaryResponseDto> employees =  employeeService.getSummaryEmployees();
        return APIResponseBuilder.list(employees, employees.size());
    }

    @GetMapping(value = "/list/{designation}")
    public ResponseEntity<APISuccessResponse<List<EmployeeSummaryResponseDto>>> get(
            @PathVariable String designation) {
        List<EmployeeSummaryResponseDto> employees = employeeService.getEmployeesByDesignation(designation);
        return APIResponseBuilder.list(employees, employees.size());
    }



    @PostMapping
    public ResponseEntity<APISuccessResponse<EmployeeDetailResponseDto>> add(
            @RequestBody @Valid EmployeeCreateRequestDto employeeCreateRequest)
    {
        EmployeeDetailResponseDto savedEmployee = employeeService.createEmployee(employeeCreateRequest);
        return APIResponseBuilder.created(savedEmployee, savedEmployee.getId());
    }

    @PutMapping
    public ResponseEntity<APISuccessResponse<EmployeeDetailResponseDto>> update(
            @RequestBody @Valid EmployeeUpdateRequestDto employeeUpdateRequestDto)
    {
        EmployeeDetailResponseDto updatedEmployee = employeeService.updateEmployee(employeeUpdateRequestDto);
        return APIResponseBuilder.updated(updatedEmployee,updatedEmployee.getId());
    }

    @PostMapping("/deactivate")
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivateBranches(@RequestBody List<Integer> ids) {
        List<Integer> deactivatedIds = employeeService.deactivateEmployee(ids);
        return APIResponseBuilder.ok(
                deactivatedIds,
                Map.of("status", "deactivated", "count", deactivatedIds.size())
        );    }

    @PostMapping("/activate")
    public ResponseEntity<APISuccessResponse<List<Integer>>> activateBranches(@RequestBody List<Integer> ids) {
        List<Integer> activatedIds = employeeService.activateEmployees(ids);
        return APIResponseBuilder.ok(
                activatedIds,
                Map.of("status", "activated", "count", activatedIds.size())
        );    }

}
