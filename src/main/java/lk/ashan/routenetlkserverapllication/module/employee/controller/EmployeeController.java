package lk.ashan.routenetlkserverapllication.module.employee.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeeService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing employee-related operations.
 * Provides endpoints for viewing, adding, updating, and deactivating employees.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Retrieves a list of employees based on the provided parameters.
     *
     * @param params A map of query parameters for filtering employees.
     * @return A response entity containing a list of employee details.
     */
    @PreAuthorize("hasAuthority('employee-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeeDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<EmployeeDetailResponseDto> employees = params.isEmpty()
                ?employeeService.getEmployees()
                : employeeService.searchEmployee(params);

        return APIResponseBuilder.list(employees, employees.size());
    }

    /**
     * Retrieves a summary list of all employees.
     *
     * @return A response entity containing a list of employee summaries.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries",produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeeSummaryDto>>> get() {
        List<EmployeeSummaryDto> employees =  employeeService.getSummaryEmployees();
        return APIResponseBuilder.list(employees, employees.size());
    }

    /**
     * Retrieves a summary list of employees filtered by designation.
     *
     * @param designation The designation to filter employees by.
     * @return A response entity containing a list of employee summaries.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries/{designation}")
    public ResponseEntity<APISuccessResponse<List<EmployeeSummaryDto>>> get(
            @PathVariable String designation) {
        List<EmployeeSummaryDto> employees = employeeService.getEmployeesByDesignation(designation);
        return APIResponseBuilder.list(employees, employees.size());
    }

    /**
     * Adds a new employee.
     *
     * @param employeeCreateRequest The request body containing employee creation details.
     * @return A response entity containing the details of the created employee.
     */
    @PreAuthorize("hasAuthority('employee-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<EmployeeDetailResponseDto>> add(
            @RequestBody @Valid EmployeeCreateRequestDto employeeCreateRequest)
    {
        EmployeeDetailResponseDto savedEmployee = employeeService.createEmployee(employeeCreateRequest);
        return APIResponseBuilder.created(savedEmployee, savedEmployee.getId());
    }

    /**
     * Updates an existing employee.
     *
     * @param employeeUpdateRequestDto The request body containing employee update details.
     * @return A response entity containing the details of the updated employee.
     */
    @PreAuthorize("hasAuthority('employee-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<EmployeeDetailResponseDto>> update(
            @RequestBody @Valid EmployeeUpdateRequestDto employeeUpdateRequestDto)
    {
        EmployeeDetailResponseDto updatedEmployee = employeeService.updateEmployee(employeeUpdateRequestDto);
        return APIResponseBuilder.updated(updatedEmployee,updatedEmployee.getId());
    }

    /**
     * Deactivates a list of employees by their IDs.
     *
     * @param ids A list of employee IDs to deactivate.
     * @return A response entity containing the list of deactivated employee IDs and additional metadata.
     */
    @PreAuthorize("hasAuthority('employee-delete')")
    @DeleteMapping("/deactivate")
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivateBranches(@RequestBody List<Integer> ids) {
        List<Integer> deactivatedIds = employeeService.deactivateEmployee(ids);
        return APIResponseBuilder.ok(
                deactivatedIds,
                Map.of("status", "deactivated", "count", deactivatedIds.size())
        );
    }
}
