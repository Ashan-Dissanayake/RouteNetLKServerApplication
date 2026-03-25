package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.DesignationMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.repository.DesignationRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignationService {

    private final DesignationRepository designationRepository;
    private final DesignationMapper designationMapper;

    @Transactional(readOnly = true)
    public List<DesignationDto> getDesignations(){
       return designationMapper.toDtoList(designationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Designation getById(Integer id) {
        return designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Designation not found"
                ));
    }

}
