package lk.ashan.routenetlkserverapllication.module.privilege.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.PrivilegeAssignRequestDto;
import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.RolePrivilegeResponseDto;
import lk.ashan.routenetlkserverapllication.module.privilege.service.PrivilegeService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping(value = "/privileges")
@RequiredArgsConstructor
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @PreAuthorize("hasAuthority('privilge-view')")
    @GetMapping(path ="/{roleId}", produces = "application/json")
    public ResponseEntity<APISuccessResponse<RolePrivilegeResponseDto>> get(
            @PathVariable Integer roleId
    ) {
        RolePrivilegeResponseDto privilege = privilegeService.getRolePrivilege(roleId);
        return APIResponseBuilder.ok(privilege);
    }

    @PreAuthorize("hasAuthority('privilge-assign')")
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

    @PreAuthorize("hasAuthority('privilge-revoke')")
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
