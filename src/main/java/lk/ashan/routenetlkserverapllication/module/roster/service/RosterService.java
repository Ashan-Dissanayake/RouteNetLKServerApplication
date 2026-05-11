package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RosterService{

    private final RosterRepository rosterRepository;
    private final RosterMapper rosterMapper;
    private final RosterShiftAutomationService rosterShiftAutomationService;

    @Transactional(readOnly = true)
    public List<RosterSummaryDto> getRosterSummary(){
        List<Roster> rosters = rosterRepository.findAll();
        return rosterMapper.toSummaryDto(rosters);
    }

    @Transactional
    public RosterSummaryDto createRoster(RosterRequestDto requestDto) {
        // 1. Validate the 7-day range
        if (!requestDto.getDostartofweek().plusDays(6).equals(requestDto.getDoendofweek())) {
            throw new BusinessRuleViolationException("A roster must span exactly 7 days.");
        }

        // 2. Check for overlapping rosters
        boolean exists = rosterRepository.existsByBranchIdAndDateRange(
                requestDto.getBranch().getId(),
                requestDto.getDostartofweek(),
                requestDto.getDoendofweek()
        );

        if (exists) {
            throw new BusinessRuleViolationException("A roster already exists for this branch.");
        }

        // 3. Map to Entity
        Roster roster = rosterMapper.toEntity(requestDto);

        // 4. Populate and Save everything at once
        rosterShiftAutomationService.generateWeeklyRosterSlots(roster);

        // 5. Return the DTO
        return rosterMapper.toSummaryDto(roster);
    }
}
