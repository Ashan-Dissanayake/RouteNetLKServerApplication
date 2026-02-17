package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RosterService {

    private final RosterRepository rosterRepository;
    private final RosterMapper rosterMapper;

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


}
