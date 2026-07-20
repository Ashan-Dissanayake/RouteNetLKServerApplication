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

@CrossOrigin
@RestController
@RequestMapping(value = "/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PreAuthorize("hasAuthority('user-role-view')")
    @GetMapping(value = "/{userId}/roles",produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<UserRoleResponseDto>>> get(
            @PathVariable Integer userId)
    {
        List<UserRoleResponseDto> userRoles = userRoleService.getUserRoles(userId);
        return APIResponseBuilder.list(userRoles, userRoles.size());
    }

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

    @PreAuthorize("hasAuthority('user-role-revoke')")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<APISuccessResponse<String>> removeRole(
            @PathVariable Integer userId, @PathVariable Integer roleId
    ) {
        userRoleService.removeRole(userId, roleId);
        return APIResponseBuilder.ok("Role removed successfully");
    }

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
