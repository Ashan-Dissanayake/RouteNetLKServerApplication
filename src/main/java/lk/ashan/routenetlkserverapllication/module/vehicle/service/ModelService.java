package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ModelDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.ModelMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.FuelType;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Model;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.ModelRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<ModelDto> getModels(){
        return modelMapper.toDtoList(modelRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Model getById(Integer id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fuel type not found"
                ));
    }
}
