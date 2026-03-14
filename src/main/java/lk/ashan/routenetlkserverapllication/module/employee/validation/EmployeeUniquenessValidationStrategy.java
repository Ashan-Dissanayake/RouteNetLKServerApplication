package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeUniquenessValidationStrategy implements EmployeeValidationStrategy {

    private final EmployeeRepository employeeRepository;

    @Override
    public void validateCreate(EmployeeValidationContext context) {
        if (employeeRepository.existsByNumber(context.getNumber())) {
            throw new ResourceExistsException("Employee number already exists.");
        }
        if (employeeRepository.existsByNic(context.getNic())) {
            throw new ResourceExistsException("NIC already exists.");
        }
        if (employeeRepository.existsByMobile(context.getMobile())) {
            throw new ResourceExistsException("Mobile number already exists.");
        }
        if (employeeRepository.existsByEmail(context.getEmail())) {
            throw new ResourceExistsException("Email already exists.");
        }

        validateMobileAndEmergencyContact(context.getMobile(), context.getEmergencyContact());
        
        if (employeeRepository.existsByEmergencycontact(context.getMobile())) {
            throw new BusinessRuleViolationException("Mobile number already used as emergency contact by another employee.");
        }
        if (employeeRepository.existsByMobile(context.getEmergencyContact())) {
             throw new BusinessRuleViolationException("Emergency contact already used as another employee’s mobile number.");
        }
    }

    @Override
    public void validateUpdate(EmployeeValidationContext context) {
        if (employeeRepository.existsByNumberAndIdNot(context.getNumber(), context.getId())) {
             throw new ResourceExistsException("Employee number already exists.");
        }
        if (employeeRepository.existsByNicAndIdNot(context.getNic(), context.getId())) {
             throw new ResourceExistsException("NIC already exists.");
        }
        if (employeeRepository.existsByMobileAndIdNot(context.getMobile(), context.getId())) {
             throw new ResourceExistsException("Mobile number already exists.");
        }
        if (employeeRepository.existsByEmailAndIdNot(context.getEmail(), context.getId())) {
             throw new ResourceExistsException("Email already exists.");
        }

        validateMobileAndEmergencyContact(context.getMobile(), context.getEmergencyContact());

        if (employeeRepository.existsByEmergencycontactAndIdNot(context.getMobile(), context.getId())) {
             throw new BusinessRuleViolationException("Mobile number already used as emergency contact by another employee.");
        }
        if (employeeRepository.existsByMobileAndIdNot(context.getEmergencyContact(), context.getId())) {
             throw new BusinessRuleViolationException("Emergency contact already used as another employee’s mobile number.");
        }
    }

    private void validateMobileAndEmergencyContact(String mobile, String emergency) {
        if (mobile.equals(emergency)) {
            throw new BusinessRuleViolationException("Employee mobile number and emergency contact cannot be the same.");
        }
    }
}
