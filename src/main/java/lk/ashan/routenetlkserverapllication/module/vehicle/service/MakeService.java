package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.MakeRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.MakeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.MakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MakeService {

    private final MakeRepository makeRepository;
    private final MakeMapper makeMapper;

    public List<MakeRequestDto> getMakes(){
       return makeMapper.toDtoList(makeRepository.findAll());
    }

}
