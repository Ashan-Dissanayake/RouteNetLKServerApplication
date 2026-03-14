package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class GenderNicValidationStrategy implements EmployeeValidationStrategy {

    @Override
    public void validateCreate(EmployeeValidationContext context) {
         String gender = extractGender(context.getNic());
         if (!context.getGenderName().equalsIgnoreCase(gender)) {
            throw new BusinessRuleViolationException("Gender not match with given NIC");
        }
    }

    @Override
    public void validateUpdate(EmployeeValidationContext context) {
        String gender = extractGender(context.getNic());
        if (context.getGenderName() != null) {
             if (!context.getGenderName().equalsIgnoreCase(gender)) {
                throw new BusinessRuleViolationException("Gender not match with given NIC");
            }
        }
    }

    private String extractGender(String nic) {
        if (nic == null) throw new IllegalArgumentException("NIC cannot be null");
        nic = nic.trim().toUpperCase();

        if (nic.matches("^\\d{12}$")) {
            int dayCode = Integer.parseInt(nic.substring(4, 7));
            return (dayCode > 500) ? "Female" : "Male";
        }
        if (nic.matches("^\\d{9}[VvXx]$")) {
            int dayCode = Integer.parseInt(nic.substring(2, 5));
            return (dayCode > 500) ? "Female" : "Male";
        }
        throw new IllegalArgumentException("Invalid NIC format");
    }
}
