package lk.ashan.routenetlkserverapllication.module.employee.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.employee.state.EmployeeStateFactory;
import lk.ashan.routenetlkserverapllication.module.employee.state.EmployeeStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.employee.validation.EmployeeContextBuilder;
import lk.ashan.routenetlkserverapllication.module.employee.validation.EmployeeValidationContext;
import lk.ashan.routenetlkserverapllication.module.employee.validation.EmployeeValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableBranchFilter;
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
    private final DesignationService designationService;
    private final DepartmentService departmentService;
    private final EmployeeTypeService employeeTypeService;
    private final EmployeeMapper employeeMapper;
    private final NumberGeneratorService numberGeneratorService;

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
    public List<EmployeeSummaryDto> getSummaryEmployees(){
        return employeeMapper.toSummaryDetailList(employeeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryDto> getEmployeesByDesignation(String designation) {
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

    @Transactional(readOnly = true)
    public Employee getById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    @DisableSoftDeleteFilter
    @DisableBranchFilter
    public EmployeeDetailResponseDto createEmployee(@NotNull EmployeeCreateRequestDto request) {

        EmployeeValidationContext context = employeeContextBuilder.buildForCreate(request);
        validationStrategies.forEach(strategy -> strategy.validateCreate(context));

        Employee employee = employeeMapper.toEntity(request);

        EmployeeStatus initialStatus = employeeStatusService.getByName(request.getEmployeestatus().getName());
        employeeStateFactory.getState(initialStatus.getName())
                .validateInitial();
        employee.setEmployeestatus(initialStatus);
        employee.setNumber(numberGeneratorService.nextEmployeeNumber());
        employee.setEmail(employee.getCallingname()+numberGeneratorService.nextEmployeeNumber()+"@sltb.lk");

        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    @DisableBranchFilter
    public EmployeeDetailResponseDto updateEmployee(@NotNull EmployeeUpdateRequestDto request) {
        Employee existing = employeeRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        EmployeeValidationContext context = employeeContextBuilder.buildForUpdate(request);

        validationStrategies.forEach(strategy -> strategy.validateUpdate(context));

        Employee entity = employeeMapper.updateEntityFromDto(request, existing);

        if (request.getEmployeestatus().getId()!=null){
            EmployeeStatus targetStatus = employeeStatusService.getById(request.getEmployeestatus().getId());
            employeeStateTransitionHandler.transitionTo(entity, targetStatus);
        }

        if (request.getEmployeetype().getId()!=null){
            EmployeeType targetType = employeeTypeService.getById(request.getEmployeetype().getId());
            entity.setEmployeetype(targetType);
        }

        if (request.getDesignation().getId()!=null){
            Designation targetDesignation = designationService.getById(request.getDesignation().getId());
            entity.setDesignation(targetDesignation);

        }

        if (request.getDepartment().getId()!=null){
            Department targetDepartment = departmentService.getById(request.getDepartment().getId());
            entity.setDepartment(targetDepartment);

        }
        return employeeMapper.toDto(entity);
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

}
