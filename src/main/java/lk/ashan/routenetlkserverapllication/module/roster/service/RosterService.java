package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterConfirmationRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterConfirmationResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterStatusRepository;
import lk.ashan.routenetlkserverapllication.module.roster.state.RosterState;
import lk.ashan.routenetlkserverapllication.module.roster.state.RosterStateFactory;
import lk.ashan.routenetlkserverapllication.module.roster.validation.RosterValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
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
    private final List<RosterValidationStrategy> validationStrategies;
    private final RosterStatusRepository rosterStatusRepository;
    private final RosterAssignmentRepository rosterAssignmentRepository;
    private final RosterStateFactory rosterStateFactory;

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
    public RosterConfirmationResponseDto confirmRoster(
            @Valid @NotNull RosterConfirmationRequestDto confirmationRequestDto) {

        Roster existingRoster =
                rosterRepository.findByBranch_IdAndRosterstatus_NameAndDoroster(
                        confirmationRequestDto.getBranchId(),
                        "Solved",
                        confirmationRequestDto.getDate()
                );

        if (existingRoster == null) {
            throw new ResourceNotFoundException("No SOLVED roster found for given branch and date");
        }

        RosterState rosterState =
                rosterStateFactory.getState(existingRoster.getRosterstatus().getName());

        if (confirmationRequestDto.getConfirm()) {

            Rosterstatus lockedStatus =
                    rosterStatusRepository.findByName("Locked");

            rosterState.transitionTo(existingRoster, lockedStatus);

            existingRoster.setRosterstatus(lockedStatus);

            rosterAssignmentRepository
                    .updateStatusByRosterId(existingRoster.getId(), "Confirmed");
        }

        return RosterConfirmationResponseDto.builder()
                .rosterId(existingRoster.getId())
                .branchId(existingRoster.getBranch().getId())
                .date(existingRoster.getDoroster())
                .message("Roster confirmed successfully")
                .build();
    }



}

