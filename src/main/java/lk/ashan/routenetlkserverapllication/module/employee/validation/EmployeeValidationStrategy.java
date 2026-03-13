package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;

public interface EmployeeValidationStrategy {
    void validateCreate(EmployeeCreateRequestDto request);
    void validateUpdate(EmployeeUpdateRequestDto request);
}
