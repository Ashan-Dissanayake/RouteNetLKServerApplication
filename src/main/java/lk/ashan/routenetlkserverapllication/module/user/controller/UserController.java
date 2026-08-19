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

/**
 * Controller for managing user-related operations.
 * Provides endpoints for user management such as viewing, adding, updating,
 * activating/deactivating, and managing passwords.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a list of users. If parameters are provided, performs a search based on the parameters.
     *
     * @param params A map of search parameters.
     * @return A response entity containing a list of user details and the total count.
     */
    @PreAuthorize("hasAuthority('user-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<UserDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<UserDetailResponseDto> users = params.isEmpty()
                ? userService.getUsers()
                : userService.searchUsers(params);

        return APIResponseBuilder.list(users, users.size());
    }

    /**
     * Adds a new user.
     *
     * @param userCreateRequestDto The details of the user to be created.
     * @return A response entity containing the created user's details.
     */
    @PreAuthorize("hasAuthority('user-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<UserDetailResponseDto>> add(
            @RequestBody @Valid UserCreateRequestDto userCreateRequestDto)
    {
        UserDetailResponseDto savedUser = userService.createUser(userCreateRequestDto);
        return APIResponseBuilder.created(savedUser, savedUser.getId());
    }

    /**
     * Updates an existing user.
     *
     * @param updateRequestDto The updated details of the user.
     * @return A response entity containing the updated user's details.
     */
    @PreAuthorize("hasAuthority('user-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<UserDetailResponseDto>> update(
            @RequestBody @Valid UserUpdateRequestDto updateRequestDto)
    {
        UserDetailResponseDto updatedUser = userService.updateUser(updateRequestDto);
        return APIResponseBuilder.updated(updatedUser, updatedUser.getId());
    }

    /**
     * Activates or deactivates a user.
     *
     * @param userActiveDeactiveDto The details of the user to be activated or deactivated.
     * @return A response entity containing a success message.
     */
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

    /**
     * Changes the password of a user.
     *
     * @param userId The ID of the user whose password is to be changed.
     * @param request The details of the new password.
     * @return A response entity containing a success message.
     */
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

    /**
     * Resets the password of a user.
     *
     * @param userId The ID of the user whose password is to be reset.
     * @param resetPasswordRequestDto The details of the reset password.
     * @return A response entity containing a success message.
     */
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
