package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.SeatingcapacityResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.SeatingcapacityMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.SeatingcapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingcapacityService {

    private final SeatingcapacityRepository seatingcapacityRepository;
    private final SeatingcapacityMapper seatingcapacityMapper;

    public List<SeatingcapacityResponseDto> getSeatingcapacities(){
       return seatingcapacityMapper.toDtoList(seatingcapacityRepository.findAll());
    }

}
