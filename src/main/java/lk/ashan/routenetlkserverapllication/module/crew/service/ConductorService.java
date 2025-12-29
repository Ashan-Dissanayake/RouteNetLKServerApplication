package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.dto.*;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.ConductorMapper;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;

    public List<ConductorDetailResponseDto> getConductors(){
       return conductorMapper.toDtoList(conductorRepository.findAll());
    }
}
