package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeStatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeStatusMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Employee Status operations.
 * Provides methods to retrieve employee statuses by various criteria.
 */
@Service
@RequiredArgsConstructor
public class EmployeeStatusService {

    private final EmployeeStatusRepository employeeStatusRepository;
    private final EmployeeStatusMapper employeeStatusMapper;

    /**
     * Retrieves all employee statuses as a list of DTOs.
     *
     * @return a list of {@link EmployeeStatusDto} representing all employee statuses.
     */
    @Transactional(readOnly = true)
    public List<EmployeeStatusDto> getEmployeeStatuses(){
       return employeeStatusMapper.toDtoList(employeeStatusRepository.findAll());
    }

    /**
     * Retrieves an employee status by its name.
     *
     * @param name the name of the employee status to retrieve.
     * @return the {@link EmployeeStatus} with the specified name.
     * @throws ResourceNotFoundException if no employee status with the given name is found.
     */
    @Transactional(readOnly = true)
    public EmployeeStatus getByName(String name) {
        return employeeStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee status '" + name + "' not found"
                ));
    }

    /**
     * Retrieves an employee status by its ID.
     *
     * @param id the ID of the employee status to retrieve.
     * @return the {@link EmployeeStatus} with the specified ID.
     * @throws ResourceNotFoundException if no employee status with the given ID is found.
     */
    @Transactional(readOnly = true)
    public EmployeeStatus getById(Integer id) {
        return employeeStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee status not found"
                ));
    }

}
