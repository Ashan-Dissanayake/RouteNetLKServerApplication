package lk.ashan.routenetlkserverapllication.module.roster.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.validation.context.RosterCreationContext;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RosterOverlapValidationStrategy implements RosterCreationStrategy{

    private final RosterRepository rosterRepository;


    @Override
    public void validate(RosterCreationContext context) {

        // Check if another active roster exists for same branch with overlapping dates
        List<Roster> existingRosters = rosterRepository
                .findActiveRostersByBranchAndDateRange(
                        context.getBranchId(),
                        context.getDostartofweek(),
                        context.getDoendofweek()
                );

        // If updating (context has currentRosterId), exclude current roster
        if (context.getCurrentRosterId() != null) {
            existingRosters.removeIf(r ->
                    r.getId().equals(context.getCurrentRosterId())
            );
        }

        if (!existingRosters.isEmpty()) {
            Roster conflicting = existingRosters.get(0);
            throw new BusinessRuleViolationException(
                    "A roster already exists for this branch covering week " +
                            conflicting.getDostartofweek() + " to " +
                            conflicting.getDoendofweek()
            );
        }

    }
}
