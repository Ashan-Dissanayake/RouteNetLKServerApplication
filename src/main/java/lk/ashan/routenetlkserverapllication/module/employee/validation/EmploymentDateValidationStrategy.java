package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmploymentDateValidationStrategy implements EmployeeValidationStrategy {

    @Override
    public void validateCreate(EmployeeValidationContext context) {
        String type = context.getEmployeeTypeName().trim().toLowerCase();
        LocalDate doj = context.getDateOfJoining();
        int currentYear = LocalDate.now().getYear();

        if (doj == null) {
            throw new BusinessRuleViolationException("Date of Joining is required.");
        }

        if ((type.equals("probationers") || type.equals("contract")) && doj.getYear() < currentYear) {
            throw new BusinessRuleViolationException(
                    String.format("%s employees cannot have a Date of Joining older than the current year (%d).",
                            context.getEmployeeTypeName(), currentYear)
            );
        }
    }

    @Override
    public void validateUpdate(EmployeeValidationContext context) {
        // Similar validation for updates if type changes
    }
}
