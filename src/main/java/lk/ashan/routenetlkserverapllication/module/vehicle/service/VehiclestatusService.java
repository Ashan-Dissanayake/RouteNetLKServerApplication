package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehiclestatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehiclestatusMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehiclestatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiclestatusService {

    private final VehiclestatusRepository vehiclestatusRepository;
    private final VehiclestatusMapper vehiclestatusMapper;

    public List<VehiclestatusDto> getVehiclestatuss(){
       return vehiclestatusMapper.toDtoList(vehiclestatusRepository.findAll());
    }

}
