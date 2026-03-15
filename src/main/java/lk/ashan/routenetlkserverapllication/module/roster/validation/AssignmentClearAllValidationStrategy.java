package lk.ashan.routenetlkserverapllication.module.roster.validation;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates all suggestions can only be cleared for DRAFT rosters
 */
@Component
@RequiredArgsConstructor
public class AssignmentClearAllValidationStrategy {

    private final RosterRepository rosterRepository;

    public void validate(Integer rosterId) {

        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        "Roster not found with ID: " + rosterId
                ));

        // Can only clear for DRAFT rosters
        if (!"DRAFT".equalsIgnoreCase(roster.getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only clear suggestions for DRAFT rosters. " +
                            "Current status: " + roster.getRosterstatus().getName()
            );
        }
    }
}

