package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

    private static final Map<String, List<String>> VALID_COMBINATIONS = Map.of(
            "operations",
            List.of(
                    "driver",
                    "conductor",
                    "operations officer",
                    "depot manager"
            ),

            "engineering and technical",
            List.of(
                    "mechanic",
                    "maintenance officer",
                    "supervisory"
            ),

            "stores department",
            List.of(
                    "inventory officer",
                    "clerical"
            ),

            "administrative",
            List.of(
                    "assistant manager"
            ),

            "finance and revenue",
            List.of(
                    "finance officer",
                    "clerical"
            )
    );

    private final EmployeeRepository employeeRepository;

    public void validateCreate(EmployeeCreateRequestDto request) {

        validateDesignationRules(
                request.getDepartment().getName(),
                request.getDesignation().getName(),
                request.getGender().getName()
        );

        validateEmploymentDate(request.getEmployeetype().getName(), request.getDoj());
        validateGenderAgainstNic(request.getGender().getName(), request.getNic());
        validateUniqueness(request.getNic(), request.getMobile(), request.getEmergencycontact());
    }

    public void validateUpdate(EmployeeUpdateRequestDto request) {

        if (request.getDepartment() != null && request.getDesignation() != null && request.getGender() != null) {
            validateDesignationRules(
                    request.getDepartment().getName(),
                    request.getDesignation().getName(),
                    request.getGender().getName()
            );
        }

        if (request.getEmployeetype() != null && request.getDoj() != null) {
            validateEmploymentDate(request.getEmployeetype().getName(), request.getDoj());
        }

        if (request.getGender() != null && request.getNic() != null) {
            validateGenderAgainstNic(request.getGender().getName(), request.getNic());
        }

        validateUniqueness(
                request.getId(),
                request.getNic(),
                request.getMobile(),
                request.getEmergencycontact()
        );
    }

    private void validateDesignationRules(String departmentName, String designationName, String genderName) {

        String department = departmentName.trim().toLowerCase();
        String designation = designationName.trim().toLowerCase();

        List<String> allowedDesignations = VALID_COMBINATIONS.get(department);

        if (allowedDesignations == null || !allowedDesignations.contains(designation)) {

            throw new BusinessRuleViolationException(
                    String.format(
                            "Invalid combination: %s cannot belong to %s department.",
                            designationName,
                            departmentName
                    )
            );
        }

        validateFemaleDriver(genderName, designationName);
    }

    private void validateFemaleDriver(String genderName, String designationName) {

        if ("female".equalsIgnoreCase(genderName) && "driver".equalsIgnoreCase(designationName)) {
            throw new BusinessRuleViolationException(
                    "Female employees cannot be assigned to the Driver designation."
            );
        }
    }

    private void validateUniqueness(String nic, String mobile, String emergencyContact) {

        validateMobileAndEmergencyContact(mobile, emergencyContact);

        if (employeeRepository.existsByNic(nic)) {
            throw new ResourceExistsException(
                    "NIC already exists."
            );
        }

        if (employeeRepository.existsByMobile(mobile)) {
            throw new ResourceExistsException(
                    "Mobile number already exists."
            );
        }

        if (employeeRepository.existsByEmergencycontact(emergencyContact)) {
            throw new ResourceExistsException(
                    "Emergency contact already exists."
            );
        }

        if (employeeRepository.existsByEmergencycontact(mobile)) {
            throw new BusinessRuleViolationException(
                    "Mobile number already used as emergency contact by another employee."
            );
        }

        if (employeeRepository.existsByMobile(emergencyContact)) {
            throw new BusinessRuleViolationException(
                    "Emergency contact already used as another employee's mobile number."
            );
        }
    }

    private void validateUniqueness(Integer id, String nic, String mobile, String emergencyContact) {

        validateMobileAndEmergencyContact(mobile, emergencyContact);

        if (employeeRepository.existsByNicAndIdNot(nic, id)) {
            throw new ResourceExistsException(
                    "NIC already exists."
            );
        }

        if (employeeRepository.existsByMobileAndIdNot(mobile, id)) {
            throw new ResourceExistsException(
                    "Mobile number already exists."
            );
        }

        if (employeeRepository.existsByEmergencycontactAndIdNot(emergencyContact, id)) {
            throw new ResourceExistsException(
                    "Emergency contact already exists."
            );
        }

        if (employeeRepository.existsByEmergencycontactAndIdNot(mobile, id)) {
            throw new BusinessRuleViolationException(
                    "Mobile number already used as emergency contact by another employee."
            );
        }

        if (employeeRepository.existsByMobileAndIdNot(emergencyContact, id)) {
            throw new BusinessRuleViolationException(
                    "Emergency contact already used as another employee's mobile number."
            );
        }
    }

    private void validateMobileAndEmergencyContact(String mobile, String emergencyContact) {

        if (mobile != null && emergencyContact != null && mobile.equals(emergencyContact)) {
            throw new BusinessRuleViolationException(
                    "Mobile number and emergency contact cannot be the same."
            );
        }
    }

    private void validateGenderAgainstNic(String gender, String nic) {

        String nicGender = extractGender(nic);

        if (!gender.equalsIgnoreCase(nicGender)) {
            throw new BusinessRuleViolationException(
                    "Gender does not match the given NIC."
            );
        }
    }

    private String extractGender(String nic) {

        if (nic == null) {
            throw new BusinessRuleViolationException(
                    "NIC cannot be null."
            );
        }

        String normalizedNic = nic.trim().toUpperCase();

        int dayOfYear;

        if (normalizedNic.matches("\\d{12}")) {

            dayOfYear = Integer.parseInt(
                    normalizedNic.substring(4, 7)
            );

        } else if (normalizedNic.matches("\\d{9}[VX]")) {

            dayOfYear = Integer.parseInt(
                    normalizedNic.substring(2, 5)
            );

        } else {
            throw new BusinessRuleViolationException(
                    "Invalid NIC format."
            );
        }

        return dayOfYear > 500 ? "Female" : "Male";
    }

    private void validateEmploymentDate(String employeeType, LocalDate dateOfJoining) {

        String type = employeeType.trim().toLowerCase();
        int currentYear = LocalDate.now().getYear();

        if ((type.equals("probationers") || type.equals("contract")) && dateOfJoining.getYear() < currentYear) {

            throw new BusinessRuleViolationException(
                    String.format(
                            "%s employees cannot have a Date of Joining older than the current year (%d).",
                            employeeType,
                            currentYear
                    )
            );
        }
    }
}