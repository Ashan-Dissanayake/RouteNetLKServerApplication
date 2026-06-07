package lk.ashan.routenetlkserverapllication.module.vehicleservice.service;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper.VehicleServicePriorityMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePriorityDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServicePriority;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.repository.VehicleServicePriorityRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VehicleServicePriorityService {

    private final VehicleServicePriorityRepository vehicleServicePriorityRepository;
    private final VehicleServicePriorityMapper vehicleServicePriorityMapper;

    @Transactional(readOnly = true)
    public List<VehicleServicePriorityDto> getVehicleServicePriorities(){
        return vehicleServicePriorityMapper.toDtoList(vehicleServicePriorityRepository.findAll());
    }

    @Transactional(readOnly = true)
    public VehicleServicePriority getById(Integer id) {
        return vehicleServicePriorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Priority not found"
                ));
    }

    @Transactional(readOnly = true)
    public VehicleServicePriority getByName(String name) {
        return vehicleServicePriorityRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Priority not found"
                ));
    }

}
