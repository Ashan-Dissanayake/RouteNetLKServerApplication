package lk.ashan.routenetlkserverapllication.module.vehicleservice.service;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper.VehicleServiceStatusMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceStatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.repository.VehicleServiceStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VehicleServiceStatusService {

    private final VehicleServiceStatusRepository vehicleServiceStatusRepository;
    private final VehicleServiceStatusMapper vehicleServiceStatusMapper;

    @Transactional(readOnly = true)
    public List<VehicleServiceStatusDto> getVehicleServiceStatus(){
        return vehicleServiceStatusMapper.toDtoList(vehicleServiceStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public VehicleServiceStatus getById(Integer id) {
        return vehicleServiceStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Priority not found"
                ));
    }

    @Transactional(readOnly = true)
    public VehicleServiceStatus getByName(String name) {
        return vehicleServiceStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Priority not found"
                ));
    }

}
