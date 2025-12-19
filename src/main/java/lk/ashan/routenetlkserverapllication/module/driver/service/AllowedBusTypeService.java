package lk.ashan.routenetlkserverapllication.module.driver.service;

import lk.ashan.routenetlkserverapllication.module.driver.dto.AllowedBusTypeDto;
import lk.ashan.routenetlkserverapllication.module.driver.mapper.AllowedBusTypeMapper;
import lk.ashan.routenetlkserverapllication.module.driver.repository.AllowedBusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllowedBusTypeService {

    private final AllowedBusTypeRepository allowedBusTypeRepository;
    private final AllowedBusTypeMapper allowedBusTypeMapper;

    public List<AllowedBusTypeDto> getAllowedBusTypes(){
       return allowedBusTypeMapper.toDtoList(allowedBusTypeRepository.findAll());
    }

}
