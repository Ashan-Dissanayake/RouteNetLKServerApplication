package lk.ashan.routenetlkserverapllication.module.employee.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ContactConflictException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidDepartmentDesignationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidNICGenderException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

        String expectedEmail = generateEmail(request.getCallingname(),request.getNumber());

        if (request.getEmail() == null || !request.getEmail().equalsIgnoreCase(expectedEmail)) {
            request.setEmail(expectedEmail); // ensure correct format
        }

        String gender = extractGender(request.getNic());
        if (!request.getGender().getName().equalsIgnoreCase(gender)){
            throw new InvalidNICGenderException("Gender not match with given NIC");
        }

        validateDepartmentDesignation(request.getDepartment().getName(),request.getDesignation().getName());

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

        mobileAndEmergencyContactConflictVerification(employee);

    }

    private void mobileAndEmergencyContactConflictVerification(@NotNull EmployeeCreateRequestDto employee) {
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

    private String generateEmail(String callingName, String number) {
        if (callingName == null || number == null)
            throw new IllegalArgumentException("Calling name and employee number required");
        return callingName.toLowerCase() + "." + number + "@sltb.lk";
    }

    private static String extractGender(String nic) {
        if (nic == null) {
            throw new IllegalArgumentException("NIC cannot be null");
        }

        nic = nic.trim().toUpperCase();

        // --- New NIC format (12 digits) ---
        if (nic.matches("^\\d{12}$")) {
            int dayCode = Integer.parseInt(nic.substring(4, 7));
            return (dayCode > 500) ? "Female" : "Male";
        }

        // --- Old NIC format (9 digits + letter) ---
        else if (nic.matches("^\\d{9}[VvXx]$")) {
            int dayCode = Integer.parseInt(nic.substring(2, 5));
            return (dayCode > 500) ? "Female" : "Male";
        }

        throw new IllegalArgumentException("Invalid NIC format");
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


    private static final Map<String, List<String>> VALID_COMBINATIONS = Map.of(
            "operations (traffic)", List.of("driver", "conductor", "depot manager"),
            "engineering and technical", List.of("mechanic", "supervisory"),
            "administrative", List.of("assistant manager", "supervisory", "clerical"),
            "finance and revenue", List.of("clerical"),
            "stores department", List.of("clerical")
    );


}
