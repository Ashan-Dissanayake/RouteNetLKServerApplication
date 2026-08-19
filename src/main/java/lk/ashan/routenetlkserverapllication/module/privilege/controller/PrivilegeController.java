package lk.ashan.routenetlkserverapllication.module.privilege.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeAssignRequestDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeResponseDto;
import lk.ashan.routenetlkserverapllication.module.privilege.service.PrivilegeService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


/**
 * Controller for managing privileges. Provides endpoints for viewing, assigning,
 * and revoking privileges.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/privileges")
@RequiredArgsConstructor
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    /**
     * Retrieves a list of privileges. If query parameters are provided,
     * privileges are filtered based on the parameters.
     *
     * @param params A map of query parameters for filtering privileges.
     * @return A ResponseEntity containing a list of privileges and their count.
     */
    @PreAuthorize("hasAuthority('privilege-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PrivilegeResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<PrivilegeResponseDto> privileges = params.isEmpty()
                ? privilegeService.getPrivileges()
                : privilegeService.searchPrivileges(params);

        return APIResponseBuilder.list(privileges, privileges.size());
    }

    /**
     * Assigns privileges to a specific role.
     *
     * @param roleId The ID of the role to which privileges will be assigned.
     * @param requestDto The request body containing the privileges to assign.
     * @return A ResponseEntity indicating the success of the operation.
     */
    @PreAuthorize("hasAuthority('privilege-assign')")
    @PostMapping("/{roleId}/assign")
    public ResponseEntity<APISuccessResponse<String>> assignPrivileges(
            @PathVariable Integer roleId,
            @Valid @RequestBody PrivilegeAssignRequestDto requestDto
    ) {
        privilegeService.assignPrivileges(roleId, requestDto);
        return APIResponseBuilder.ok(
                "Privileges assigned successfully"
        );
    }

    /**
     * Revokes a specific privilege from a role.
     *
     * @param roleId The ID of the role from which the privilege will be revoked.
     * @param privilegeId The ID of the privilege to revoke.
     * @return A ResponseEntity indicating the success of the operation.
     */
    @PreAuthorize("hasAuthority('privilege-revoke')")
    @DeleteMapping("/{roleId}/{privilegeId}/revoke")
    public ResponseEntity<APISuccessResponse<String>> removePrivilege(
            @PathVariable Integer roleId, @PathVariable Integer privilegeId
    ) {
        privilegeService.removePrivilege(roleId, privilegeId);
        return APIResponseBuilder.ok(
                "Privilege removed successfully"
        );
    }

}
