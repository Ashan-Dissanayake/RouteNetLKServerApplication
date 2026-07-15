package lk.ashan.routenetlkserverapllication.module.branch.validation;


/**
 * Interface defining validation strategies for branch operations.
 */
public interface BranchValidationStrategy {

    /**
     * Validates the context for creating a branch.
     *
     * @param context the context containing branch creation details
     * @throws IllegalArgumentException if validation fails
     */
    void validateCreate(BranchContext context);

    /**
     * Validates the context for updating a branch.
     *
     * @param context the context containing branch update details
     * @throws IllegalArgumentException if validation fails
     */
    void validateUpdate(BranchContext context);
}
