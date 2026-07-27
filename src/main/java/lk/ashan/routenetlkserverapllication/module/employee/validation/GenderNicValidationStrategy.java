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
        if (nic == null) {
            throw new BusinessRuleViolationException("NIC cannot be null.");
        }

        String cleanedNic = nic.trim().toUpperCase();

        int dayCode;

        if (cleanedNic.matches("^\\d{12}$")) {
            dayCode = Integer.parseInt(cleanedNic.substring(4, 7));
        }
        else if (cleanedNic.matches("^\\d{9}[V]$")) {
            dayCode = Integer.parseInt(cleanedNic.substring(2, 5));
        }
        else {
            throw new BusinessRuleViolationException("Invalid NIC format.");
        }

        return dayCode > 500 ? "Female" : "Male";
    }

}
