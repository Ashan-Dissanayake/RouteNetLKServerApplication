package lk.ashan.routenetlkserverapllication.module.branch.validation;


public interface BranchValidationStrategy {
    void validateCreate(BranchContext context);
    void validateUpdate(BranchContext context);
}
