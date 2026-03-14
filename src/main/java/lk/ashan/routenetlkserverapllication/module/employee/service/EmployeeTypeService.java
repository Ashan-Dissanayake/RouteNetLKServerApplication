package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeTypeDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeTypeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeTypeService {
    
    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeTypeMapper employeeTypeMapper;
    
    public List<EmployeeTypeDto> getEmployeeTypes(){
       return employeeTypeMapper.toDtoList(employeeTypeRepository.findAll());
    }
    
}
