package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterConfirmationRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterConfirmationResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
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
import org.drools.base.rule.Collect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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
        return processRosterConfirmation(
                confirmationRequestDto,
                true,
                "Locked",
                "Confirmed",
                "Roster confirmed successfully"
        );
    }

    @Transactional
    public RosterConfirmationResponseDto rejectRoster(
            @Valid @NotNull RosterConfirmationRequestDto confirmationRequestDto) {
        return processRosterConfirmation(
                confirmationRequestDto,
                false,
                "Rejected",
                "Cancelled",
                "Roster rejected successfully"
        );
    }

    // ----- Common private method -----
    private RosterConfirmationResponseDto processRosterConfirmation(
            RosterConfirmationRequestDto request,
            boolean confirmFlag,
            String targetRosterStatusName,
            String targetAssignmentStatusName,
            String message
    ) {
        // 1. Fetch roster
        Roster existingRoster = rosterRepository
                .findByBranch_IdAndRosterstatus_NameAndDoroster(
                        request.getBranchId(),
                        "Solved",
                        request.getDate()
                );

        if (existingRoster == null) {
            throw new ResourceNotFoundException(
                    "No SOLVED roster found for given branch and date"
            );
        }

        // 2. Only process if request matches the confirm/reject logic
        if (request.getConfirm() == confirmFlag) {

            // ---- Roster state transition ----
            RosterState rosterState =
                    rosterStateFactory.getState(existingRoster.getRosterstatus().getName());

            Rosterstatus targetRosterStatus = rosterStatusRepository.findByName(targetRosterStatusName);

            rosterState.transitionTo(existingRoster, targetRosterStatus);
            existingRoster.setRosterstatus(targetRosterStatus);

            // ---- Assignment state transition ----
            Rosterassignementstatus targetAssignmentStatus =
                    rosterAssignmentStatusRepository.findByName(targetAssignmentStatusName);

            existingRoster.getRosterassignements().forEach(assignment -> {
                RosterAssignmentState assignmentState =
                        rosterAssignmentStateFactory.getState(
                                assignment.getRosterassignementstatus().getName()
                        );

                assignmentState.transitionTo(assignment, targetAssignmentStatus);
                assignment.setRosterassignementstatus(targetAssignmentStatus);
            });
        }

        // 3. Build response
        return RosterConfirmationResponseDto.builder()
                .rosterId(existingRoster.getId())
                .branchId(existingRoster.getBranch().getId())
                .date(existingRoster.getDoroster())
                .message(message)
                .build();
    }

}

