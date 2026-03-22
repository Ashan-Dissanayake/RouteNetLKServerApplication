package lk.ashan.routenetlkserverapllication.module.branch.state;


import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class BranchStateFactory {

    private final Map<String, BranchState> stateMap;

    public BranchStateFactory(
            BranchActiveState active,
            BranchSuspendedState suspended,
            BranchClosedState closed
    ) {
        this.stateMap = Map.of(
                "ACTIVE", active,
                "SUSPENDED", suspended,
                "CLOSED", closed
        );
    }

    public BranchState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        BranchState state = stateMap.get(normalized);
        if (state == null) {
            throw new IllegalArgumentException("Unknown branch status: " + statusName);
        }
        return state;
    }
}
