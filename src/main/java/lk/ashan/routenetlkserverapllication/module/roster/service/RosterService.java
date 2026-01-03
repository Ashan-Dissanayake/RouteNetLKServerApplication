package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RosterService {

    private final RosterRepository rosterRepository;
    private final RosterMapper rosterMapper;

    public List<RosterDto> getRosters() {
        List<Roster> rosters =rosterRepository.findAll();
        return rosterMapper.toDetailList(rosters);
    }
}

