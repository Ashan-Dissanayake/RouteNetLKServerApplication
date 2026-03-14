package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeStatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeStatusMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeStatusService {
    
    private final EmployeeStatusRepository employeeStatusRepository;
    private final EmployeeStatusMapper employeeStatusMapper;
    
    public List<EmployeeStatusDto> getEmployeeStatuses(){
       return employeeStatusMapper.toDtoList(employeeStatusRepository.findAll());
    }

    public EmployeeStatus getByName(String name) {
        return employeeStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee status '" + name + "' not found"
                ));
    }


}
