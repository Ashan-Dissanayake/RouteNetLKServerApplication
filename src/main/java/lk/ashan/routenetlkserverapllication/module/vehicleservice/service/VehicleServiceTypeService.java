package lk.ashan.routenetlkserverapllication.module.vehicleservice.service;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper.VehicleServiceTypeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceType;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.repository.VehicleServiceTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VehicleServiceTypeService {

    private final VehicleServiceTypeRepository vehicleServiceTypeRepository;
    private final VehicleServiceTypeMapper vehicleServiceTypeMapper;

    @Transactional(readOnly = true)
    public List<VehicleServiceTypeDto> getVehicleServiceTypes(){
        return vehicleServiceTypeMapper.toDtoList(vehicleServiceTypeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public VehicleServiceType getById(Integer id) {
        return vehicleServiceTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Type not found"
                ));
    }

    @Transactional(readOnly = true)
    public VehicleServiceType getByName(String name) {
        return vehicleServiceTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Type not found"
                ));
    }

}
