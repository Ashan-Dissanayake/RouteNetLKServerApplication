package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ModelDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.ModelMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

    public List<ModelDto> getModels(){
       return modelMapper.toDtoList(modelRepository.findAll());
    }

}
