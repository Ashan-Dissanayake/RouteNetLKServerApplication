package lk.ashan.ntcserverapllication.module.branch.service;

import lk.ashan.ntcserverapllication.module.branch.model.Province;
import lk.ashan.ntcserverapllication.module.branch.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Autowired
    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    public List<Province> getProvinces() {

        List<Province> provinces = this.provinceRepository.findAll();

        return provinces.stream().map(
                province -> {
                    Province d = new Province();
                    d.setId(province.getId());
                    d.setName(province.getName());
                    return d;
                }
        ).collect(Collectors.toList());
    }
}
