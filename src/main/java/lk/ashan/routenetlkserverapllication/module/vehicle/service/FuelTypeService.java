package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.FueltypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.FueltypeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.FuelType;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.FuelTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;
    private final FueltypeMapper fuelTypeMapper;

    @Transactional(readOnly = true)
    public List<FueltypeDto> getFuelTypes(){
       return fuelTypeMapper.toDtoList(fuelTypeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public FuelType getById(Integer id) {
        return fuelTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fuel type not found"
                ));
    }
}
