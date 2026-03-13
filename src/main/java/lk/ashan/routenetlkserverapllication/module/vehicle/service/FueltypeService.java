package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.FueltypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.FueltypeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.FuelTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FueltypeService {

    private final FuelTypeRepository fueltypeRepository;
    private final FueltypeMapper fueltypeMapper;

    public List<FueltypeDto> getFueltypes(){
       return fueltypeMapper.toDtoList(fueltypeRepository.findAll());
    }

}
