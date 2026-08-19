package lk.ashan.routenetlkserverapllication.module.privilege.controller;

import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.ModuleDto;
import lk.ashan.routenetlkserverapllication.module.privilege.service.ModuleService;
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
 * Controller for managing module-related operations.
 * Provides endpoints for retrieving module summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    /**
     * Retrieves a list of module summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of ModuleDto objects
     *         and the total count of modules.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ModuleDto>>> get() {
        List<ModuleDto> modules = moduleService.getModules();
        return APIResponseBuilder.list(modules, modules.size());
    }

}
