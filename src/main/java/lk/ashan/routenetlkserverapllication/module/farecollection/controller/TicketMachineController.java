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

@CrossOrigin
@RestController
@RequestMapping(value = "/ticket-machines")
@RequiredArgsConstructor
public class TicketMachineController {

    private final TicketMachineService ticketMachineService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TicketMachineDto>>> get() {
        List<TicketMachineDto> ticketMachines = ticketMachineService.getTicketMachines();
        return APIResponseBuilder.list(ticketMachines, ticketMachines.size());
    }

}
