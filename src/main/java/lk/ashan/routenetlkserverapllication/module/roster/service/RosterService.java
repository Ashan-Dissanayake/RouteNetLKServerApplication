package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
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
    private final RosterMapper rosterMapper;

    public List<RosterDetailResponseDto> getRosters() {
        List<Roster> rosters =rosterRepository.findAll();
        return rosterMapper.toDtoList(rosters);
    }

    public  List<RosterDetailResponseDto> searchRosters(@NotNull HashMap<String, String> params){

        String branchId = params.get("ssbranch");
        String statusId = params.get("ssstatus");

        Stream<Roster> rosterStream = rosterRepository.findAll().stream();

        if (branchId!=null)
            rosterStream = rosterStream.filter(r->r.getBranch().getId() == Integer.parseInt(branchId));
        if (statusId!=null)
            rosterStream = rosterStream.filter(r->r.getRosterstatus().getId() == Integer.parseInt(statusId));

        return rosterMapper.toDtoList(rosterStream.collect(Collectors.toList()));
    }

    @Transactional
    public RosterDetailResponseDto createRoster(@NotNull RosterCreateRequestDto createRequestDto){
        validateUniqueness(createRequestDto);

        Roster roster = rosterMapper.toEntity(createRequestDto);
        Roster savedRoster = rosterRepository.save(roster);
        return rosterMapper.toDto(savedRoster);
    }

    public void validateUniqueness(RosterCreateRequestDto createRequestDto){
        boolean isExisted = rosterRepository.existsByBranch_IdAndDoroster(createRequestDto.getBranch().getId(),createRequestDto.getDoroster());
        if (isExisted){
            throw new ResourceExistsException("Roster already existed");
        }
    }

}

