package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeTypeDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeeTypeService;
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

/**
 * Controller for managing employee types.
 * Provides endpoints for retrieving employee type summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/employee-types")
@RequiredArgsConstructor
public class EmployeeTypeController {

    private final EmployeeTypeService employeetypeService;

    /**
     * Retrieves a list of employee type summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of EmployeeTypeDto objects
     *         and the total count of employee types.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeeTypeDto>>> get() {
        List<EmployeeTypeDto> employeeTypes = employeetypeService.getEmployeeTypes();
        return APIResponseBuilder.list(employeeTypes, employeeTypes.size());
    }

}
