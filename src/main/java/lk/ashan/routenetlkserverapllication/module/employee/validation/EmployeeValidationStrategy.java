package lk.ashan.routenetlkserverapllication.module.employee.validation;



public interface EmployeeValidationStrategy {
    void validateCreate(EmployeeValidationContext validationContext);
    void validateUpdate(EmployeeValidationContext validationContext);
}
