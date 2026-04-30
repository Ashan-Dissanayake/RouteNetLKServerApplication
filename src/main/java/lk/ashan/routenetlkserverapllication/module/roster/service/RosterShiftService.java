package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterShiftMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RosterShiftService {

    private final RosterShiftRepository rosterShiftRepository;
    private final RosterShiftMapper rosterShiftMapper;

    public List<RosterShiftDetailResponseDto> getRosterShiftRosterId(Integer rosterId){
        List<RosterShift> rosterShifts = rosterShiftRepository.findByRoster_Id(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster shifts not found for roster id: " + rosterId
                ));
        return rosterShiftMapper.toDtoList(rosterShifts);
    }

}
