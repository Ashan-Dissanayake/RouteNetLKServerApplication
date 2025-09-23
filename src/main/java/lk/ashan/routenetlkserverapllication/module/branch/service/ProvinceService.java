package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.ProvinceDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.ProvinceMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;

    public List<ProvinceDto> getProvinces() {
        return provinceMapper.toDtoList(provinceRepository.findAll());
    }

}
