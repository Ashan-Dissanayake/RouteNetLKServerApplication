package lk.ashan.routenetlkserverapllication.module.trip.service;

import lk.ashan.routenetlkserverapllication.module.trip.mapper.OpCalenderMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OpCalenderSummaryDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Opcalender;
import lk.ashan.routenetlkserverapllication.module.trip.repository.OpCalenderRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing operation calendars.
 */
@Service
@RequiredArgsConstructor
public class OpCalenderService {

    private final OpCalenderRepository opCalenderRepository;
    private final OpCalenderMapper opCalenderMapper;

    /**
     * Retrieves all operation calendars and maps them to DTOs.
     *
     * @return a list of {@link OpCalenderSummaryDto} representing the operation calendars.
     */
    @Transactional(readOnly = true)
    public List<OpCalenderSummaryDto> getOpCalenders() {
        return opCalenderMapper.toDtoList(opCalenderRepository.findAll());
    }

    /**
     * Retrieves an operation calendar by its name.
     *
     * @param name the name of the operation calendar to retrieve.
     * @return the {@link Opcalender} entity corresponding to the given name.
     * @throws ResourceNotFoundException if no operation calendar with the given name is found.
     */
    public Opcalender getByName(String name) {
        return opCalenderRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operation Calender '" + name + "' not found"
                ));
    }
}
