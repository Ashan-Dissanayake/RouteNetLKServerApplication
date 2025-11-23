package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.ServicetypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.ServicetypeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.ServicetypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicetypeService {

    private final ServicetypeRepository servicetypeRepository;
    private final ServicetypeMapper servicetypeMapper;

    public List<ServicetypeDto> getServicetypes(){
       return servicetypeMapper.toDtoList(servicetypeRepository.findAll());
    }

}
