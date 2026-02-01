package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterConfirmationRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterConfirmationResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterAssignmentStatusRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterStatusRepository;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterState;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterStateFactory;
import lk.ashan.routenetlkserverapllication.module.roster.state.rosterassignment.RosterAssignmentState;
import lk.ashan.routenetlkserverapllication.module.roster.state.rosterassignment.RosterAssignmentStateFactory;
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
    private final RosterAssignmentStatusRepository rosterAssignmentStatusRepository;
    private final RosterAssignmentRepository rosterAssignmentRepository;
    private final RosterStateFactory rosterStateFactory;
    private final RosterAssignmentStateFactory rosterAssignmentStateFactory;

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
            throw new ResourceNotFoundException(
                    "No SOLVED roster found for given branch and date"
            );
        }

        if (confirmationRequestDto.getConfirm()) {

            // ---- Roster state transition ----
            RosterState rosterState =
                    rosterStateFactory.getState(
                            existingRoster.getRosterstatus().getName()
                    );

            Rosterstatus lockedStatus =
                    rosterStatusRepository.findByName("Locked");

            rosterState.transitionTo(existingRoster, lockedStatus);
            existingRoster.setRosterstatus(lockedStatus);

            // ---- Assignment state transition ----
            Rosterassignementstatus confirmedStatus =
                    rosterAssignmentStatusRepository.findByName("Confirmed");

            existingRoster.getRosterassignements().forEach(assignment -> {

                RosterAssignmentState assignmentState =
                        rosterAssignmentStateFactory.getState(
                                assignment.getRosterassignementstatus().getName()
                        );

                // Validate transition
                assignmentState.transitionTo(assignment, confirmedStatus);

                // Apply transition
                assignment.setRosterassignementstatus(confirmedStatus);
            });
        }

        return RosterConfirmationResponseDto.builder()
                .rosterId(existingRoster.getId())
                .branchId(existingRoster.getBranch().getId())
                .date(existingRoster.getDoroster())
                .message("Roster confirmed successfully")
                .build();
    }
}

