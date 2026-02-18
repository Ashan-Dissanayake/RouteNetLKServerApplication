package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterStatusRepository;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterState;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterStatusFactory;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RosterService {

    private final RosterRepository rosterRepository;
    private final RosterStatusRepository rosterStatusRepository;
    private final RosterMapper rosterMapper;

    private final RosterStatusFactory rosterStatusFactory;

    public List<RosterDetailResponseDto> getRosters(){
        return rosterMapper.toDtoList(rosterRepository.findAll());
    }

    public List<RosterDetailResponseDto> searchRosters(@NotNull HashMap<String, String> params) {

        List<Roster> rosters = rosterRepository.findAll();

            String branchName = params.get("ssname");
            String rosterStatusId= params.get("ssrosterstatus");

            Stream<Roster> rosterStream = rosters.stream();

            if(branchName!=null)rosterStream = rosterStream.filter(r->r.getBranch().getName().toLowerCase().contains(branchName.toLowerCase()));
            if(rosterStatusId!=null)rosterStream = rosterStream.filter(r->r.getRosterstatus().getId()==Integer.parseInt(rosterStatusId));

            return rosterMapper.toDtoList( rosterStream.collect(Collectors.toList()));
    }

    @Transactional
    @DisableSoftDeleteFilter
    public RosterDetailResponseDto createRoster(RosterCreateRequestDto createRequestDto){

        Rosterstatus draftStatus = rosterStatusRepository
                .findByName("Draft")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster status not found: Draft"
                ));

        RosterState state = rosterStatusFactory.getState(draftStatus.getName());
        state.validateInitial();

        Roster roster = rosterMapper.toEntity(createRequestDto);
        return rosterMapper.toDto(rosterRepository.save(roster));
    }

}
