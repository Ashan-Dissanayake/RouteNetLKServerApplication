package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehiclestatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehiclestatusMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiclestatusService {

    private final VehicleStatusRepository vehiclestatusRepository;
    private final VehiclestatusMapper vehiclestatusMapper;

    public List<VehiclestatusDto> getVehiclestatuss(){
       return vehiclestatusMapper.toDtoList(vehiclestatusRepository.findAll());
    }

    public VehicleStatus getByName(String name) {
        return vehiclestatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee status '" + name + "' not found"
                ));
    }

}
