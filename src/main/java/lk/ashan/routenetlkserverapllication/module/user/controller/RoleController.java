package lk.ashan.routenetlkserverapllication.module.user.controller;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.RoleDto;
import lk.ashan.routenetlkserverapllication.module.user.service.RoleService;
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
 * Controller for managing roles in the application.
 * Provides endpoints for retrieving role summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Retrieves a list of role summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of RoleDto objects
     *         and the total count of roles.
     * @throws SecurityException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RoleDto>>> get() {
        List<RoleDto> roles = roleService.getRoles();
        return APIResponseBuilder.list(roles, roles.size());
    }
}
