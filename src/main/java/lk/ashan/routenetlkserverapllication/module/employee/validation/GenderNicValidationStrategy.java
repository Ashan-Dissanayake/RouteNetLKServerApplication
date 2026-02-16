package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class GenderNicValidationStrategy implements EmployeeValidationStrategy {

    @Override
    public void validateCreate(EmployeeCreateRequestDto request) {
         String gender = extractGender(request.getNic());
         if (!request.getGender().getName().equalsIgnoreCase(gender)) {
            throw new BusinessRuleViolationException("Gender not match with given NIC");
        }
    }

    @Override
    public void validateUpdate(EmployeeUpdateRequestDto request) {
        // Typically gender/NIC validation is needed on update too if NIC changes
        String gender = extractGender(request.getNic());
        // Note: UpdateDTO might not always have the gender object fully populated if it's just an ID reference,
        // but looking at usage, it seems full object is passed. Assuming Gender object is present in Request.
        if (request.getGender() != null && request.getGender().getName() != null) {
             if (!request.getGender().getName().equalsIgnoreCase(gender)) {
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
