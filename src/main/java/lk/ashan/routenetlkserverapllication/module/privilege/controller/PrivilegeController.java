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


@CrossOrigin
@RestController
@RequestMapping(value = "/privileges")
@RequiredArgsConstructor
public class PrivilegeController {

    private final PrivilegeService privilegeService;

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
