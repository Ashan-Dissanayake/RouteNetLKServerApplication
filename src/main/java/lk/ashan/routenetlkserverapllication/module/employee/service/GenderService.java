package lk.ashan.routenetlkserverapllication.module.employee.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.GenderDto;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.GenderMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Gender;
import lk.ashan.routenetlkserverapllication.module.employee.repository.GenderRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;
    private final GenderMapper genderMapper;

    @Transactional(readOnly = true)
    public List<GenderDto> getGenders(){
       return genderMapper.toDtoList(genderRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Gender getById(Integer id) {
        return genderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gender not found"
                ));
    }


}
