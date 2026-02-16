package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmploymentDateValidationStrategy implements EmployeeValidationStrategy {

    @Override
    public void validateCreate(EmployeeCreateRequestDto request) {
        String type = request.getEmployeetype().getName().trim().toLowerCase();
        LocalDate doj = request.getDoj();
        int currentYear = LocalDate.now().getYear();

        if (doj == null) {
            throw new BusinessRuleViolationException("Date of Joining is required.");
        }

        if ((type.equals("probationers") || type.equals("contract")) && doj.getYear() < currentYear) {
            throw new BusinessRuleViolationException(
                    String.format("%s employees cannot have a Date of Joining older than the current year (%d).",
                            request.getEmployeetype().getName(), currentYear)
            );
        }
    }

    @Override
    public void validateUpdate(EmployeeUpdateRequestDto request) {
        // Similar validation for updates if type changes
    }
}
