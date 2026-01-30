package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterAssignmentScheduler;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterAssignmentService;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterAssignmentSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.validation.RosterValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RosterService {

    private final RosterRepository rosterRepository;
    private final RosterMapper rosterMapper;
    private final List<RosterValidationStrategy> validationStrategies;
    private final RosterAssignmentScheduler rosterAssignmentScheduler;


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
    public RosterDetailResponseDto createRoster(@Valid @NotNull RosterCreateRequestDto createRequestDto){
        validationStrategies.forEach(strategy -> strategy.validateCreate(createRequestDto));

        Roster roster = rosterMapper.toEntity(createRequestDto);
        Roster savedRoster = rosterRepository.save(roster);
        return rosterMapper.toDto(savedRoster);
    }

    @Transactional
    public RosterAssignmentSolution solveRoster(String branchID,String date){

        return rosterAssignmentScheduler.manualAssignment(Integer.parseInt(branchID), LocalDate.parse(date));
    }


}

