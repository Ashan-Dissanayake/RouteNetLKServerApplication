package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.ShiftMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    public List<ShiftDto> getShifts() {
        List<Shift> shifts =shiftRepository.findAll();
        return shiftMapper.toDtolList(shifts);
    }

}

