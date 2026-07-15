package lk.ashan.routenetlkserverapllication.module.user.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.user.service.UserService;
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
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private  final UserService userService;

    @PreAuthorize("hasAuthority('user-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<UserDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<UserDetailResponseDto> users = params.isEmpty()
                ?userService.getUsers()
                :userService.searchUsers(params);

        return APIResponseBuilder.list(users, users.size());
    }

    @PreAuthorize("hasAuthority('user-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<UserDetailResponseDto>> add(
            @RequestBody @Valid UserCreateRequestDto userCreateRequestDto)
    {
        UserDetailResponseDto savedUser = userService.createUser(userCreateRequestDto);
        return APIResponseBuilder.created(savedUser, savedUser.getId());
    }

    @PreAuthorize("hasAuthority('user-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<UserDetailResponseDto>> update(
            @RequestBody @Valid UserUpdateRequestDto updateRequestDto)
    {
        UserDetailResponseDto updatedUser = userService.updateUser(updateRequestDto);
        return APIResponseBuilder.updated(updatedUser, updatedUser.getId());
    }

    @PreAuthorize("hasAuthority('user-delete')")
    @PutMapping("/activate-or-deactivate-user")
    public ResponseEntity<APISuccessResponse<String>> activateOrDeactivateUser(
            @Valid @RequestBody UserActiveDeactiveDto userActiveDeactiveDto)
    {
        userService.activateOrDeactivateUser(userActiveDeactiveDto);

        return APIResponseBuilder.ok(
                "User " + userActiveDeactiveDto.getUsername()
                 + " status updated successfully"
        );
    }

    @PreAuthorize("hasAuthority('user-change-password')")
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<APISuccessResponse<String>> changePassword(
            @PathVariable Integer userId,
            @Valid @RequestBody ChangePasswordRequestDto request
    ) {
        userService.changePassword(userId, request);

        return APIResponseBuilder.ok(
                "Password changed successfully"
        );
    }

    @PreAuthorize("hasAuthority('user-reset-password')")
    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<APISuccessResponse<String>> resetPassword(
            @PathVariable Integer userId,
            @Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto
    ) {
        userService.resetPassword(userId, resetPasswordRequestDto);
        return APIResponseBuilder.ok(
                "Password reset successfully"
        );
    }
}
