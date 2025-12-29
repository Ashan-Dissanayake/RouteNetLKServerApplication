package lk.ashan.routenetlkserverapllication.module.employee.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.model.Crewstatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeestatus;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeDetailResponseDto> getEmployees(){
       return employeeMapper.toDtoList(employeeRepository.findAll());
    }

    public List<EmployeeDetailResponseDto> searchEmployee(@NotNull HashMap<String, String> params) {

        String fullName = params.get("ssname");
        String number = params.get("ssnumber");
        String departmentid = params.get("ssdepartment");

        Stream<Employee> employeeStream = employeeRepository.findAll().stream();

        if (fullName != null)
            employeeStream = employeeStream.filter(e -> e.getFullname().toLowerCase().contains(fullName.toLowerCase()));
        if (number != null) employeeStream = employeeStream.filter(e -> e.getNumber().equalsIgnoreCase(number));
        if (departmentid != null)
            employeeStream = employeeStream.filter(e -> e.getDepartment().getId() == Integer.parseInt(departmentid));

        return employeeMapper.toDtoList(employeeStream.collect(Collectors.toList()));

    }

    public List<EmployeeSummaryResponseDto> getSummaryEmployees(){
        return employeeMapper.toSummaryDetailList(employeeRepository.findAll());
    }

    public  List<EmployeeSummaryResponseDto> getEmployeesByDesignation(String designation){
        return employeeMapper.toSummaryDetailList(employeeRepository.findEmployeesWithoutDriver(designation));
    }

    @Transactional
    @DisableSoftDeleteFilter
    public EmployeeDetailResponseDto createEmployee(@NotNull EmployeeCreateRequestDto request) {

        // --- Ensure email auto-generated correctly ---
        ensureEmailFormat(request);

        // --- Validate NIC & Gender consistency ---
        validateGenderAgainstNIC(request);

        // --- Validate Department vs Designation mapping ---
        validateDepartmentDesignation(request.getDepartment().getName(), request.getDesignation().getName());

        // --- Validate Gender vs Designation mapping ---
        validateFemaleEmployeesNotDriver(request);

        // --- Validate employment type & joining date ---
        validateEmploymentDate(request);

        // --- Validate uniqueness across branch & personal contact details ---
        validateEmployeeUniquenessForCreate(request);

        // --- Persist ---
        Employee employee = employeeMapper.toEntity(request);
        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public EmployeeDetailResponseDto updateEmployee(@NotNull EmployeeUpdateRequestDto request) {

        Employeestatus currentStatus = employeeRepository.findByMyId(request.getId()).getEmployeestatus();

        validateStatusTransition(currentStatus.getName(),request.getEmployeestatus().getName());
        validateEmployeeUniquenessForUpdate(request);

        Employee employee = employeeMapper.toEntity(request);
        Employee updated = employeeRepository.save(employee);

        return employeeMapper.toDto(updated);

    }

    @Transactional
    public List<Integer> deactivateEmployee(List<Integer> employeeIds) {
        List<Employee> employees = employeeRepository.findAllById(employeeIds);

        if (employees.isEmpty())
            throw new ResourceNotFoundException("No employees found for the given IDs");

        employeeRepository.removeAll(employeeIds);

        List<Driver> drivers = driverRepository.findAllByEmployeeIds(employeeIds);
        for (Driver driver : drivers) {
            driver.setCrewstatus(new Crewstatus(4,"Inactive"));
        }
        driverRepository.saveAll(drivers);

        return employees.stream() .map(Employee::getId) .collect(Collectors.toList());
    }

    @Transactional
    public List<Integer> activateEmployees(List<Integer> branchIds) {
        List<Employee> employees = employeeRepository.findAllById(branchIds);

        if (employees.isEmpty())
            throw new ResourceNotFoundException("No employees found for the given IDs");

        employeeRepository.restoreAll(branchIds);

        return employees.stream() .map(Employee::getId) .collect(Collectors.toList());
    }

    private void ensureEmailFormat(@NotNull EmployeeCreateRequestDto request) {
        String expectedEmail = generateEmail(request.getCallingname(), request.getNumber());

        if (request.getEmail() == null || !request.getEmail().equalsIgnoreCase(expectedEmail)) {
            request.setEmail(expectedEmail);
        }
    }

    private String generateEmail(String callingName, String number) {
        if (callingName == null || number == null) {
            throw new IllegalArgumentException("Calling name and employee number required");
        }
        return callingName.toLowerCase() + "." + number + "@sltb.lk";
    }

    private void validateGenderAgainstNIC(@NotNull EmployeeCreateRequestDto request) {
        String gender = extractGender(request.getNic());

        if (!request.getGender().getName().equalsIgnoreCase(gender)) {
            throw new InvalidNICGenderException("Gender not match with given NIC");
        }
    }

    private static String extractGender(String nic) {
        if (nic == null) {
            throw new IllegalArgumentException("NIC cannot be null");
        }

        nic = nic.trim().toUpperCase();

        // --- New NIC (12 digits) ---
        if (nic.matches("^\\d{12}$")) {
            int dayCode = Integer.parseInt(nic.substring(4, 7));
            return (dayCode > 500) ? "Female" : "Male";
        }

        // --- Old NIC (9 digits + letter) ---
        if (nic.matches("^\\d{9}[VvXx]$")) {
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

    private void validateEmploymentDate(@NotNull EmployeeCreateRequestDto request) {
        String type = request.getEmployeetype().getName().trim().toLowerCase();
        LocalDate doj = request.getDoj();
        int currentYear = LocalDate.now().getYear();

        if (doj == null) {
            throw new InvalidEmploymentDateException("Date of Joining is required.");
        }

        if ((type.equals("probationers") || type.equals("contract")) && doj.getYear() < currentYear) {
            throw new InvalidEmploymentDateException(
                    String.format("%s employees cannot have a Date of Joining older than the current year (%d).",
                            request.getEmployeetype().getName(), currentYear)
            );
        }
    }

    private void validateEmployeeUniquenessForCreate(@NotNull EmployeeCreateRequestDto employee) {

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

    private void validateFemaleEmployeesNotDriver(@NotNull EmployeeCreateRequestDto employee){

        Boolean isFemale = employee.getGender().getName().equalsIgnoreCase("female");
        Boolean isDriver = employee.getDesignation().getName().equalsIgnoreCase("driver");

        if (isFemale && isDriver){
            throw  new InvalidGenderDesignationException("Female employees are not allowed to be a driver.");
        }

    }

    private void validateEmployeeUniquenessForUpdate(@NotNull EmployeeUpdateRequestDto employee) {

        // Employee number is usually autogenerated, but if it can change (e.g., branch change), check uniqueness
        if (employeeRepository.existsByNumberAndIdNot(employee.getNumber(), employee.getId())) {
            throw new ResourceExistsException("Employee number already exists.");
        }

        if (employeeRepository.existsByNicAndIdNot(employee.getNic(), employee.getId())) {
            throw new ResourceExistsException("NIC already exists.");
        }

        if (employeeRepository.existsByMobileAndIdNot(employee.getMobile(), employee.getId())) {
            throw new ResourceExistsException("Mobile number already exists.");
        }

        if (employeeRepository.existsByEmailAndIdNot(employee.getEmail(), employee.getId())) {
            throw new ResourceExistsException("Email already exists.");
        }

        mobileAndEmergencyContactConflictVerificationForUpdate(employee);
    }

    private void mobileAndEmergencyContactConflictVerificationForUpdate(@NotNull EmployeeUpdateRequestDto employee) {

        // Mobile and emergency contact cannot be same for the employee
        if (employee.getMobile().equals(employee.getEmergencycontact())) {
            throw new ContactConflictException(
                    "Employee mobile number and emergency contact cannot be the same."
            );
        }

        // Mobile cannot be used as emergency contact by another employee
        if (employeeRepository.existsByEmergencycontactAndIdNot(employee.getMobile(), employee.getId())) {
            throw new ContactConflictException(
                    "Mobile number already used as emergency contact by another employee."
            );
        }

        // Emergency contact cannot be used as mobile by another employee
        if (employeeRepository.existsByMobileAndIdNot(employee.getEmergencycontact(), employee.getId())) {
            throw new ContactConflictException(
                    "Emergency contact already used as another employee’s mobile number."
            );
        }
    }

    private static final Map<String, List<String>> VALID_TRANSITIONS = Map.of(
            "ACTIVE",List.of("SUSPEND", "RESIGNED", "ON LEAVE"),
            "SUSPEND", List.of("ACTIVE", "RESIGNED"),
            "ON LEAVE", List.of("ACTIVE", "RESIGNED"),
            "RESIGNED", List.of() // terminal state
    );

    private void validateStatusTransition(String currentStatus, String newStatus) {

        if (currentStatus == null || newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        if (currentStatus.equalsIgnoreCase(newStatus)) return;

        currentStatus = currentStatus.trim().toUpperCase();
        newStatus = newStatus.trim().toUpperCase();

        List<String> allowedStatuses = VALID_TRANSITIONS.get(currentStatus);

        if (allowedStatuses == null) {
            throw new IllegalArgumentException("Unknown current status: " + currentStatus);
        }

        if (!allowedStatuses.contains(newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus
            );
        }
    }


}
