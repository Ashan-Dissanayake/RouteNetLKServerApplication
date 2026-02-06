package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.ConditionrateDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.ConditionrateMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.ConditionRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConditionrateService {

    private final ConditionRateRepository conditionrateRepository;
    private final ConditionrateMapper conditionrateMapper;

    public List<ConditionrateDto> getConditionRates(){
       return conditionrateMapper.toDtoList(conditionrateRepository.findAll());
    }

}
