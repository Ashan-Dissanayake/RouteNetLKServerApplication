package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.DriverMapper;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public List<DriverDetailResponseDto> getDrivers(){
       return driverMapper.toDtoList(driverRepository.findAll());
    }
    


}
