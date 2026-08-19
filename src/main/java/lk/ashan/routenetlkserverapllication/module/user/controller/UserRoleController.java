package lk.ashan.routenetlkserverapllication.module.user.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserRoleAssignRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserRoleResponseDto;
import lk.ashan.routenetlkserverapllication.module.user.service.UserRoleService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user roles.
 * Provides endpoints for viewing, assigning, revoking, and replacing user roles.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    /**
     * Retrieves the roles assigned to a specific user.
     *
     * @param userId the ID of the user whose roles are to be retrieved
     * @return a response entity containing a list of user roles and their details
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     */
    @PreAuthorize("hasAuthority('user-role-view')")
    @GetMapping(value = "/{userId}/roles", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<UserRoleResponseDto>>> get(
            @PathVariable Integer userId)
    {
        List<UserRoleResponseDto> userRoles = userRoleService.getUserRoles(userId);
        return APIResponseBuilder.list(userRoles, userRoles.size());
    }

    /**
     * Assigns roles to a specific user.
     *
     * @param userId the ID of the user to whom roles are to be assigned
     * @param requestDto the request body containing the roles to be assigned
     * @return a response entity indicating the success of the operation
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     * @throws jakarta.validation.ConstraintViolationException if the request body validation fails
     */
    @PreAuthorize("hasAuthority('user-role-assign')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<APISuccessResponse<String>> assignRoles(
            @PathVariable Integer userId,
            @Valid @RequestBody UserRoleAssignRequestDto requestDto
    ) {
        userRoleService.assignRoles(userId, requestDto);
        return APIResponseBuilder.ok(
                "Roles assigned successfully"
        );
    }

    /**
     * Removes a specific role from a user.
     *
     * @param userId the ID of the user from whom the role is to be removed
     * @param roleId the ID of the role to be removed
     * @return a response entity indicating the success of the operation
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     */
    @PreAuthorize("hasAuthority('user-role-revoke')")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<APISuccessResponse<String>> removeRole(
            @PathVariable Integer userId, @PathVariable Integer roleId
    ) {
        userRoleService.removeRole(userId, roleId);
        return APIResponseBuilder.ok("Role removed successfully");
    }

    /**
     * Replaces all roles of a specific user with new roles.
     *
     * @param userId the ID of the user whose roles are to be replaced
     * @param requestDto the request body containing the new roles
     * @return a response entity indicating the success of the operation
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     * @throws jakarta.validation.ConstraintViolationException if the request body validation fails
     */
    @PreAuthorize("hasAuthority('user-role-replace')")
    @PutMapping("/{userId}/roles")
    public ResponseEntity<APISuccessResponse<String>> replaceRoles(
            @PathVariable Integer userId,
            @RequestBody @Valid UserRoleAssignRequestDto requestDto
    ) {
        userRoleService.replaceRoles(userId, requestDto);
        return APIResponseBuilder.ok(
                "User roles replaced successfully"
        );
    }

}
