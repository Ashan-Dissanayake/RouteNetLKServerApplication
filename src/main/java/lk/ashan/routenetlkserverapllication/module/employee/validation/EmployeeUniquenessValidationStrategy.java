package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeUpdateRequestDto;
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
    public void validateCreate(EmployeeCreateRequestDto request) {
        if (employeeRepository.existsByNumber(request.getNumber())) {
            throw new ResourceExistsException("Employee number already exists.");
        }
        if (employeeRepository.existsByNic(request.getNic())) {
            throw new ResourceExistsException("NIC already exists.");
        }
        if (employeeRepository.existsByMobile(request.getMobile())) {
            throw new ResourceExistsException("Mobile number already exists.");
        }
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("Email already exists.");
        }

        validateMobileAndEmergencyContact(request.getMobile(), request.getEmergencycontact());
        
        if (employeeRepository.existsByEmergencycontact(request.getMobile())) {
            throw new BusinessRuleViolationException("Mobile number already used as emergency contact by another employee.");
        }
        if (employeeRepository.existsByMobile(request.getEmergencycontact())) {
             throw new BusinessRuleViolationException("Emergency contact already used as another employee’s mobile number.");
        }
    }

    @Override
    public void validateUpdate(EmployeeUpdateRequestDto request) {
        if (employeeRepository.existsByNumberAndIdNot(request.getNumber(), request.getId())) {
             throw new ResourceExistsException("Employee number already exists.");
        }
        if (employeeRepository.existsByNicAndIdNot(request.getNic(), request.getId())) {
             throw new ResourceExistsException("NIC already exists.");
        }
        if (employeeRepository.existsByMobileAndIdNot(request.getMobile(), request.getId())) {
             throw new ResourceExistsException("Mobile number already exists.");
        }
        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), request.getId())) {
             throw new ResourceExistsException("Email already exists.");
        }

        validateMobileAndEmergencyContact(request.getMobile(), request.getEmergencycontact());

        if (employeeRepository.existsByEmergencycontactAndIdNot(request.getMobile(), request.getId())) {
             throw new BusinessRuleViolationException("Mobile number already used as emergency contact by another employee.");
        }
        if (employeeRepository.existsByMobileAndIdNot(request.getEmergencycontact(), request.getId())) {
             throw new BusinessRuleViolationException("Emergency contact already used as another employee’s mobile number.");
        }
    }

    private void validateMobileAndEmergencyContact(String mobile, String emergency) {
        if (mobile.equals(emergency)) {
            throw new BusinessRuleViolationException("Employee mobile number and emergency contact cannot be the same.");
        }
    }
}
