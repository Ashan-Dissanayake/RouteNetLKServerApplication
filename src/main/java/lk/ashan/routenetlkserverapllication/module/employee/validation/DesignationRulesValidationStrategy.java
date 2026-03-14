package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DesignationRulesValidationStrategy implements EmployeeValidationStrategy {

    private static final Map<String, List<String>> VALID_COMBINATIONS = Map.of(
            "operations (traffic)", List.of("driver", "conductor", "depot manager"),
            "engineering and technical", List.of("mechanic", "supervisory"),
            "administrative", List.of("assistant manager", "supervisory", "clerical"),
            "finance and revenue", List.of("clerical"),
            "stores department", List.of("clerical")
    );

    @Override
    public void validateCreate(EmployeeValidationContext context) {
        validateDepartmentDesignation(context.getDepartmentName(), context.getDesignationName());
        validateFemaleEmployeesNotDriver(context.getGenderName(), context.getDesignationName());
    }

    @Override
    public void validateUpdate(EmployeeValidationContext context) {
        if (context.getDepartmentName() != null && context.getDesignationName() != null) {
             validateDepartmentDesignation(context.getDepartmentName(), context.getDesignationName());
        }
        if (context.getDesignationName() != null) {
            validateFemaleEmployeesNotDriver(context.getGenderName(), context.getDesignationName());
        }
    }

    private void validateDepartmentDesignation(String department, String designation) {
        String dept = department.trim().toLowerCase();
        String desig = designation.trim().toLowerCase();

        List<String> allowed = VALID_COMBINATIONS.get(dept);
        if (allowed == null || !allowed.contains(desig)) {
            throw new BusinessRuleViolationException(
                    String.format("Invalid combination: %s cannot belong to %s department.", designation, department)
            );
        }
    }

    private void validateFemaleEmployeesNotDriver(String gender, String designation){
        boolean isFemale = gender.equalsIgnoreCase("female");
        boolean isDriver = designation.equalsIgnoreCase("driver");

        if (isFemale && isDriver){
            throw new BusinessRuleViolationException("Female employees are not allowed to be a driver.");
        }
    }
}
