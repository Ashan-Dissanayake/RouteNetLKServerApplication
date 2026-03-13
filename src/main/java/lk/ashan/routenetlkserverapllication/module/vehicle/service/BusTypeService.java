package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.BusTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.BusTypeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.BusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusTypeService {

    private final BusTypeRepository busTypeRepository;
    private final BusTypeMapper busTypeMapper;

    public List<BusTypeDto> getBusTypes(){
       return busTypeMapper.toDtoList(busTypeRepository.findAll());
    }

}
