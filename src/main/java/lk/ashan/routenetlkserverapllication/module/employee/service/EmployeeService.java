package lk.ashan.routenetlkserverapllication.module.employee.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.employee.state.EmployeeState;
import lk.ashan.routenetlkserverapllication.module.employee.state.EmployeeStateFactory;
import lk.ashan.routenetlkserverapllication.module.employee.state.EmployeeStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.employee.validation.EmployeeContextBuilder;
import lk.ashan.routenetlkserverapllication.module.employee.validation.EmployeeValidationContext;
import lk.ashan.routenetlkserverapllication.module.employee.validation.EmployeeValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final EmployeeStatusService employeeStatusService;
    private final EmployeeMapper employeeMapper;

    private final EmployeeContextBuilder employeeContextBuilder;
    private final List<EmployeeValidationStrategy> validationStrategies;
    private final EmployeeStateFactory employeeStateFactory;
    private final EmployeeStateTransitionHandler employeeStateTransitionHandler;

    @Transactional(readOnly = true)
    public List<EmployeeDetailResponseDto> getEmployees(){
       return employeeMapper.toDtoList(employeeRepository.findAll());
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<EmployeeSummaryResponseDto> getSummaryEmployees(){
        return employeeMapper.toSummaryDetailList(employeeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryResponseDto> getEmployeesByDesignation(String designation) {
        List<Employee> employees;
        if (designation.equalsIgnoreCase("driver")) {
            employees = employeeRepository.findEmployeesWithoutDriver(designation);
        } else if (designation.equalsIgnoreCase("conductor")) {
            employees = employeeRepository.findEmployeesWithoutConductor(designation);
        } else {
            employees = List.of();
        }
        return employeeMapper.toSummaryDetailList(employees);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public EmployeeDetailResponseDto createEmployee(@NotNull EmployeeCreateRequestDto request) {
        ensureEmailFormat(request);

        EmployeeValidationContext context = employeeContextBuilder.buildForCreate(request);
        validationStrategies.forEach(strategy -> strategy.validateCreate(context));

        Employee employee = employeeMapper.toEntity(request);

        EmployeeStatus initialStatus = employeeStatusService.getByName(request.getEmployeestatus().getName());
        employeeStateFactory.getState(initialStatus.getName())
                .validateInitial();
        employee.setEmployeestatus(initialStatus);

        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public EmployeeDetailResponseDto updateEmployee(@NotNull EmployeeUpdateRequestDto request) {
        EmployeeValidationContext context = employeeContextBuilder.buildForUpdate(request);
        validationStrategies.forEach(strategy -> strategy.validateUpdate(context));

        Employee existing = employeeRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employeeMapper.updateEntityFromDto(request, existing);

        EmployeeStatus targetStatus = employeeStatusService.getByName(request.getEmployeestatus().getName());
        employeeStateTransitionHandler.transitionTo(existing, targetStatus);

        Employee updated = employeeRepository.save(existing);

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
            driver.setCrewstatus(new CrewStatus(4,"Inactive"));
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

}
