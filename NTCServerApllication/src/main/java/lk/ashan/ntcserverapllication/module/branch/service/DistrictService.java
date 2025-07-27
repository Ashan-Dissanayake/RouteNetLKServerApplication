package lk.ashan.ntcserverapllication.module.branch.service;

import lk.ashan.ntcserverapllication.module.branch.dto.DistrictResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.DistrictMapper;
import lk.ashan.ntcserverapllication.module.branch.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    public List<DistrictResponse> getDistricts() {
        return districtMapper.toDistrictResponseList(districtRepository.findAll());
    }
}
