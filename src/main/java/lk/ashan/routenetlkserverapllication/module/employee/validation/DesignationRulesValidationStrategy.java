package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidDepartmentDesignationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidGenderDesignationException;
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
    public void validateCreate(EmployeeCreateRequestDto request) {
        validateDepartmentDesignation(request.getDepartment().getName(), request.getDesignation().getName());
        validateFemaleEmployeesNotDriver(request.getGender().getName(), request.getDesignation().getName());
    }

    @Override
    public void validateUpdate(EmployeeUpdateRequestDto request) {
        // Assuming Department/Designation/Gender are present in update request
        if (request.getDepartment() != null && request.getDesignation() != null) {
             validateDepartmentDesignation(request.getDepartment().getName(), request.getDesignation().getName());
        }
        if (request.getGender() != null && request.getDesignation() != null) {
            validateFemaleEmployeesNotDriver(request.getGender().getName(), request.getDesignation().getName());
        }
    }

    private void validateDepartmentDesignation(String department, String designation) {
        String dept = department.trim().toLowerCase();
        String desig = designation.trim().toLowerCase();

        List<String> allowed = VALID_COMBINATIONS.get(dept);
        if (allowed == null || !allowed.contains(desig)) {
            throw new InvalidDepartmentDesignationException(
                    String.format("Invalid combination: %s cannot belong to %s department.", designation, department)
            );
        }
    }

    private void validateFemaleEmployeesNotDriver(String gender, String designation){
        boolean isFemale = gender.equalsIgnoreCase("female");
        boolean isDriver = designation.equalsIgnoreCase("driver");

        if (isFemale && isDriver){
            throw new InvalidGenderDesignationException("Female employees are not allowed to be a driver.");
        }
    }
}
