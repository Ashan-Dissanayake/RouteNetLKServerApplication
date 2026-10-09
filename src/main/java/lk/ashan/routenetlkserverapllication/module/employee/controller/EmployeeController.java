package lk.ashan.routenetlkserverapllication.module.employee.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Employee Management", description = "Endpoints for managing employees in the system.")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Retrieves a list of employees based on the provided parameters.
     *
     * @param params A map of query parameters for filtering employees.
     * @return A response entity containing a list of employee details.
     */
    @Operation(summary = "Get all employees or search by parameters", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employees"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'employee-view' authority")
    })
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
    @Operation(summary = "Get employee summaries for dropdowns", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved summaries"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(summary = "Get employee summaries by designation", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved summaries"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
    @Operation(summary = "Add a new employee", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'employee-add' authority")
    })
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
    @Operation(summary = "Update an existing employee", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'employee-update' authority"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
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
    @Operation(summary = "Deactivate multiple employees", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees deactivated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'employee-delete' authority")
    })
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
