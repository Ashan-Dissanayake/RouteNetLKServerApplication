package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeetypeDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeetypeMapper;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeetypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeetypeService {
    
    private final EmployeetypeRepository employeetypeRepository;
    private final EmployeetypeMapper employeetypeMapper;
    
    public List<EmployeetypeDto> getEmployeetypes(){
       return employeetypeMapper.toDtoList(employeetypeRepository.findAll());
    }
    
}
