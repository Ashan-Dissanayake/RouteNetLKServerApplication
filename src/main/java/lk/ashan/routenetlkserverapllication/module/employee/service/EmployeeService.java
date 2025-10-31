package lk.ashan.routenetlkserverapllication.module.employee.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ContactConflictException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeDetailResponseDto> getEmployees(){
       return employeeMapper.toDtoList(employeeRepository.findAll());
    }

    @Transactional
    @DisableSoftDeleteFilter
    public EmployeeDetailResponseDto createEmployee(@NotNull EmployeeCreateRequestDto request) {

        validateBranchUniquenessForCreate(request);

        Employee employee = employeeMapper.toEntity(request);

        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toDto(saved);
    }


    private void validateBranchUniquenessForCreate(@NotNull EmployeeCreateRequestDto employee) {

        if (employeeRepository.existsByNumber(employee.getNumber())) {
            throw new ResourceExistsException("Employee number already exists.");
        }

        if (employeeRepository.existsByNic(employee.getNic())) {
            throw new ResourceExistsException("NIC already exists.");
        }

        if (employeeRepository.existsByMobile(employee.getMobile())) {
            throw new ResourceExistsException("Mobile number already exists.");
        }

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new ResourceExistsException("Email already exists.");
        }

        if (employee.getMobile().equals(employee.getEmergencycontact())) {
            throw new ContactConflictException(
                    "Employee mobile number and emergency contact cannot be the same."
            );
        }

        if (employeeRepository.existsByEmergencycontact(employee.getMobile())) {
            throw new ContactConflictException(
                    "Mobile number already used as emergency contact by another employee."
            );
        }

        if (employeeRepository.existsByMobile(employee.getEmergencycontact())) {
            throw new ContactConflictException(
                    "Emergency contact already used as another employee’s mobile number."
            );
        }


    }

}
