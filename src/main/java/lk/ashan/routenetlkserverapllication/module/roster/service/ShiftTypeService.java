package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftTypeDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.ShiftTypeMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shifttype;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftTypeService {

    private final ShiftTypeRepository shiftTypeRepository;
    private final ShiftTypeMapper shiftTypeMapper;

    public List<ShiftTypeDto> getShiftTypes() {
        List<Shifttype> shifts = shiftTypeRepository.findAll();
        return shiftTypeMapper.toDtoList(shifts);
    }

}

