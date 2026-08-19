package lk.ashan.routenetlkserverapllication.module.farecollection.service;

import lk.ashan.routenetlkserverapllication.module.farecollection.mapper.TicketMachineMapper;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.TicketMachineDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import lk.ashan.routenetlkserverapllication.module.farecollection.repository.TicketMachineRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Ticket Machines.
 * Provides methods to retrieve Ticket Machine data.
 */
@Service
@RequiredArgsConstructor
public class TicketMachineService {

    private final TicketMachineRepository ticketMachineRepository;
    private final TicketMachineMapper ticketMachineMapper;

    /**
     * Retrieves all Ticket Machines as a list of DTOs.
     *
     * @return a list of {@link TicketMachineDto} representing all Ticket Machines.
     */
    @Transactional(readOnly = true)
    public List<TicketMachineDto> getTicketMachines(){
        return ticketMachineMapper.toDtoList(ticketMachineRepository.findAll());
    }

    /**
     * Retrieves a Ticket Machine by its ID.
     *
     * @param id the ID of the Ticket Machine to retrieve.
     * @return the {@link TicketMachine} with the specified ID.
     * @throws ResourceNotFoundException if no Ticket Machine is found with the given ID.
     */
    @Transactional(readOnly = true)
    public TicketMachine getById(Integer id) {
        return ticketMachineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket Machine not found"
                ));
    }

    /**
     * Retrieves a Ticket Machine by its name.
     *
     * @param name the name of the Ticket Machine to retrieve.
     * @return the {@link TicketMachine} with the specified name.
     * @throws ResourceNotFoundException if no Ticket Machine is found with the given name.
     */
    @Transactional(readOnly = true)
    public TicketMachine getByName(String name) {
        return ticketMachineRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket Machine not found"
                ));
    }
}
