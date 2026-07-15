package lk.ashan.routenetlkserverapllication.module.user.controller;

import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserTypeDto;
import lk.ashan.routenetlkserverapllication.module.user.service.UserTypeService;
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

@CrossOrigin
@RestController
@RequestMapping(value = "/user-types")
@RequiredArgsConstructor
public class UserTypeController {

    private final UserTypeService userTypeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<UserTypeDto>>> get() {
        List<UserTypeDto> userTypes = userTypeService.getUserTypes();
        return APIResponseBuilder.list(userTypes, userTypes.size());
    }
}
