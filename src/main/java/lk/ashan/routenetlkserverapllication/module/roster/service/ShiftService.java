package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.mapper.ShiftMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.ShiftSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    public List<ShiftSummaryDto> getShifts(){
        List<Shift> shifts = shiftRepository.findAll();
        return shiftMapper.toDtoList(shifts);
    }

}
