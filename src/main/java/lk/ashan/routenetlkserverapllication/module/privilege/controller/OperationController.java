package lk.ashan.routenetlkserverapllication.module.privilege.controller;

import lk.ashan.routenetlkserverapllication.module.privilege.model.dto.OperationDto;
import lk.ashan.routenetlkserverapllication.module.privilege.service.OperationService;
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
@RequestMapping(value = "/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<OperationDto>>> get() {
        List<OperationDto> operations = operationService.getOperations();
        return APIResponseBuilder.list(operations, operations.size());
    }

}
