package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.DesignationMapper;
import lk.ashan.routenetlkserverapllication.module.employee.repository.DesignationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignationService {

    private final DesignationRepository designationRepository;
    private final DesignationMapper designationMapper;

    public List<DesignationDto> getDesignations(){
       return designationMapper.toDtoList(designationRepository.findAll());
    }

}
