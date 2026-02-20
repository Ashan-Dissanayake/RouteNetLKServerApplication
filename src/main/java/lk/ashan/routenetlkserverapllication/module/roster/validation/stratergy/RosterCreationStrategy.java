package lk.ashan.routenetlkserverapllication.module.roster.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.roster.validation.context.RosterCreationContext;

public interface RosterCreationStrategy {
    void validate(RosterCreationContext context);
}
