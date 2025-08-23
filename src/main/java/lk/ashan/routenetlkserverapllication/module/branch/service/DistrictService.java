package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.DistrictResponse;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.DistrictMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    public List<DistrictResponse> getDistricts() {
        return districtMapper.toDistrictResponseList(districtRepository.findAll());
    }
}
