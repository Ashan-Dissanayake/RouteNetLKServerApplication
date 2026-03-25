package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ConditionrateDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.ConditionrateMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.BusType;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.ConditionRate;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.ConditionRateRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConditionRateService {

    private final ConditionRateRepository conditionRateRepository;
    private final ConditionrateMapper conditionRateMapper;

    @Transactional(readOnly = true)
    public List<ConditionrateDto> getConditionRates(){
       return conditionRateMapper.toDtoList(conditionRateRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ConditionRate getById(Integer id) {
        return conditionRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Condition rate not found"
                ));
    }

}
