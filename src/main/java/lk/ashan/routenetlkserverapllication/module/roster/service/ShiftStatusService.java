package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.ShiftStatusMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftStatusService {

    private final ShiftStatusRepository shiftStatusRepository;
    private final ShiftStatusMapper shiftStatusMapper;

    public List<ShiftStatusDto> getShiftStatuses() {
        List<Shiftstatus> shifts = shiftStatusRepository.findAll();
        return shiftStatusMapper.toDtoList(shifts);
    }

}

