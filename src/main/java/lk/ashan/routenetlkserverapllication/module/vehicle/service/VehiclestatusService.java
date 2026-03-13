package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehiclestatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehiclestatusMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleStatusRepository;
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

}
