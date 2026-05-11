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

@Service
@RequiredArgsConstructor
public class OpCalenderService {

    private final OpCalenderRepository opCalenderRepository;
    private final OpCalenderMapper opCalenderMapper;

    @Transactional(readOnly = true)
    public List<OpCalenderSummaryDto> getOpCalenders() {
        return opCalenderMapper.toDtoList(opCalenderRepository.findAll());
    }

    public Opcalender getByName(String name) {
        return opCalenderRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operation Calender '" + name + "' not found"
                ));
    }
}
