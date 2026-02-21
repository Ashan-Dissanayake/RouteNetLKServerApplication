package lk.ashan.routenetlkserverapllication.module.roster.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.module.roster.validation.context.RosterCreationContext;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * Validates assignment business rules BEFORE OptaPlanner runs
 * These are pre-conditions for solver execution
 */
@Component
@RequiredArgsConstructor
public class RosterAssignmentPreValidationStrategy{
    
    private final RosterRepository rosterRepository;
    
    public void validate(RosterCreationContext context) {
        
        // 1. Roster must exist and be in DRAFT status
        Roster roster = rosterRepository.findById(context.getCurrentRosterId())
            .orElseThrow(() -> new BusinessRuleViolationException(
                "Roster not found with ID: " + context.getCurrentRosterId()
            ));
        
        if (!"DRAFT".equalsIgnoreCase(roster.getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                "Can only generate assignments for DRAFT rosters. " +
                "Current status: " + roster.getRosterstatus().getName()
            );
        }
    }
}
