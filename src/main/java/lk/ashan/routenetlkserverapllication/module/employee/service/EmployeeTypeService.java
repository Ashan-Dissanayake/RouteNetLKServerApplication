package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeTypeDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeTypeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeType;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Gender;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Employee Types.
 * Provides methods to retrieve employee types and fetch employee type details by ID.
 */
@Service
@RequiredArgsConstructor
public class EmployeeTypeService {

    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeTypeMapper employeeTypeMapper;

    /**
     * Retrieves a list of all employee types.
     *
     * @return a list of EmployeeTypeDto objects representing all employee types.
     */
    @Transactional(readOnly = true)
    public List<EmployeeTypeDto> getEmployeeTypes(){
       return employeeTypeMapper.toDtoList(employeeTypeRepository.findAll());
    }

    /**
     * Retrieves an employee type by its ID.
     *
     * @param id the ID of the employee type to retrieve.
     * @return the EmployeeType object corresponding to the given ID.
     * @throws ResourceNotFoundException if no employee type is found with the given ID.
     */
    @Transactional(readOnly = true)
    public EmployeeType getById(Integer id) {
        return employeeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee type not found"
                ));
    }

}
