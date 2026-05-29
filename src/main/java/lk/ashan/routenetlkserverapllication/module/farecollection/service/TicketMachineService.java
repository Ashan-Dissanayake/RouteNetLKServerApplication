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

@Service
@RequiredArgsConstructor
public class TicketMachineService {

    private final TicketMachineRepository ticketMachineRepository;
    private final TicketMachineMapper ticketMachineMapper;

    @Transactional(readOnly = true)
    public List<TicketMachineDto> getTicketMachines(){
        return ticketMachineMapper.toDtoList(ticketMachineRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TicketMachine getById(Integer id) {
        return ticketMachineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket Machine not found"
                ));
    }

    @Transactional(readOnly = true)
    public TicketMachine getByName(String name) {
        return ticketMachineRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket Machine not found"
                ));
    }


}
