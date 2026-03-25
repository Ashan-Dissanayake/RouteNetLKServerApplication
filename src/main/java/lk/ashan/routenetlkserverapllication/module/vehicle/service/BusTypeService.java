package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Department;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.BusTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.BusTypeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.BusType;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.BusTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusTypeService {

    private final BusTypeRepository busTypeRepository;
    private final BusTypeMapper busTypeMapper;

    @Transactional(readOnly = true)
    public List<BusTypeDto> getBusTypes(){
       return busTypeMapper.toDtoList(busTypeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public BusType getById(Integer id) {
        return busTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Type not found"
                ));
    }
}
