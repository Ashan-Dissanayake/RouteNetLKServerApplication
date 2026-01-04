package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterStatusMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RosterStatusService {

    private final RosterStatusRepository rosterStatusRepository;
    private final RosterStatusMapper rosterStatusMapper;

    public List<RosterStatusDto> getRosterStatuses() {
        List<Rosterstatus> rosters = rosterStatusRepository.findAll();
        return rosterStatusMapper.toDtoList(rosters);
    }

}

