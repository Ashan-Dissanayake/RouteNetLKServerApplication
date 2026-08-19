package lk.ashan.routenetlkserverapllication.module.farecollection.controller;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.TicketMachineDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.service.TicketMachineService;
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
 * Controller for managing ticket machine-related operations.
 * Provides endpoints for retrieving ticket machine summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/ticket-machines")
@RequiredArgsConstructor
public class TicketMachineController {

    private final TicketMachineService ticketMachineService;

    /**
     * Retrieves a list of ticket machine summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of TicketMachineDto objects.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TicketMachineDto>>> get() {
        List<TicketMachineDto> ticketMachines = ticketMachineService.getTicketMachines();
        return APIResponseBuilder.list(ticketMachines, ticketMachines.size());
    }

}
