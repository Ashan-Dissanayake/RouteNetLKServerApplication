package lk.ashan.routenetlkserverapllication.module.branch.state;


import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Factory class for managing and retrieving branch states.
 * This class maps branch status names to their corresponding state implementations.
 */
@Component
public class BranchStateFactory {

    private final Map<String, BranchState> stateMap;

    /**
     * Constructs a BranchStateFactory with the provided branch state implementations.
     *
     * @param active    the implementation for the "ACTIVE" branch state
     * @param suspended the implementation for the "SUSPENDED" branch state
     * @param closed    the implementation for the "CLOSED" branch state
     */
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

    /**
     * Retrieves the BranchState corresponding to the given status name.
     *
     * @param statusName the name of the branch status (e.g., "ACTIVE", "SUSPENDED", "CLOSED")
     * @return the BranchState associated with the given status name
     * @throws IllegalArgumentException if the status name is unknown or not mapped
     */
    public BranchState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        BranchState state = stateMap.get(normalized);
        if (state == null) {
            throw new IllegalArgumentException("Unknown branch status: " + statusName);
        }
        return state;
    }
}
