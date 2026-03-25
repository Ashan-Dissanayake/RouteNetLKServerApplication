package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.MakeRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.MakeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.FuelType;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Make;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.MakeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MakeService {

    private final MakeRepository makeRepository;
    private final MakeMapper makeMapper;

    @Transactional(readOnly = true)
    public List<MakeRequestDto> getMakes(){
       return makeMapper.toDtoList(makeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Make getById(Integer id) {
        return makeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fuel type not found"
                ));
    }
}
