package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehiclestatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehiclestatusMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleStatusService {

    private final VehicleStatusRepository vehicleStatusRepository;
    private final VehiclestatusMapper vehicleStatusMapper;

    @Transactional(readOnly = true)
    public List<VehiclestatusDto> getVehicleStatuses(){
       return vehicleStatusMapper.toDtoList(vehicleStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public VehicleStatus getByName(String name) {
        return vehicleStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status '" + name + "' not found"
                ));
    }

    @Transactional(readOnly = true)
    public VehicleStatus getById(Integer id) {
        return vehicleStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
