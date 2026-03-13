package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeestatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeestatusMapper;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeestatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeestatusService {
    
    private final EmployeestatusRepository employeestatusRepository;
    private final EmployeestatusMapper employeestatusMapper;
    
    public List<EmployeestatusDto> getEmployeestatuses(){
       return employeestatusMapper.toDtoList(employeestatusRepository.findAll());
    }
    
}
