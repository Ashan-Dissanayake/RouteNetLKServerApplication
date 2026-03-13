package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.GenderDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.GenderMapper;
import lk.ashan.routenetlkserverapllication.module.employee.repository.GenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;
    private final GenderMapper genderMapper;

    public List<GenderDto> getGenders(){
       return genderMapper.toDtoList(genderRepository.findAll());
    }

}
