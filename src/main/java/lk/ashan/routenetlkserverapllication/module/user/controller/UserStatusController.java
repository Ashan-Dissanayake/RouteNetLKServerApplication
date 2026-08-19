package lk.ashan.routenetlkserverapllication.module.user.controller;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserStatusDto;
import lk.ashan.routenetlkserverapllication.module.user.service.UserStatusService;
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
 * Controller for managing user statuses.
 * Provides endpoints to retrieve user status summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/user-statuses")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    /**
     * Retrieves a list of user status summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of UserStatusDto objects
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<UserStatusDto>>> get() {
        List<UserStatusDto> userStatuses = userStatusService.getUserStatuses();
        return APIResponseBuilder.list(userStatuses, userStatuses.size());
    }
}
