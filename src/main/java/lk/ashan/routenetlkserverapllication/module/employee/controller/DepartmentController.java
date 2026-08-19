package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DepartmentDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.DepartmentService;
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
 * Controller for managing department-related operations.
 * Provides endpoints for retrieving department summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Retrieves a list of department summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of DepartmentDto objects
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<DepartmentDto>>> get() {
        List<DepartmentDto> departments = departmentService.getDepartments();
        return APIResponseBuilder.list(departments, departments.size());
    }

}
