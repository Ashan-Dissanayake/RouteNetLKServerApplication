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

@Service
@RequiredArgsConstructor
public class EmployeeTypeService {
    
    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeTypeMapper employeeTypeMapper;

    @Transactional(readOnly = true)
    public List<EmployeeTypeDto> getEmployeeTypes(){
       return employeeTypeMapper.toDtoList(employeeTypeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public EmployeeType getById(Integer id) {
        return employeeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee type not found"
                ));
    }
    
}
