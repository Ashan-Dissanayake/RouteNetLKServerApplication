package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.District;
import lk.ashan.ntcserverapllication.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<District> getDistricts() {

        List<District> districts = this.districtRepository.findAll();

        return districts.stream().map(
                district -> {
                    District d = new District();
                    d.setId(district.getId());
                    d.setName(district.getName());
                    d.setProvince(district.getProvince());
                    return d;
                }
        ).collect(Collectors.toList());
    }
}
