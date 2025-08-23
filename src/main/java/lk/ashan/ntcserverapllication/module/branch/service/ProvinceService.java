package lk.ashan.ntcserverapllication.module.branch.service;

import lk.ashan.ntcserverapllication.module.branch.dto.ProvinceResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.ProvinceMapper;
import lk.ashan.ntcserverapllication.module.branch.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;

    public List<ProvinceResponse> getProvinces() {
        return provinceMapper.toProvinceResponseList(provinceRepository.findAll());
    }

}
