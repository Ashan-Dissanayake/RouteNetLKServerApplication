package lk.ashan.routenetlkserverapllication.util.seed;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branchstatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branchtype;
import lk.ashan.routenetlkserverapllication.module.branch.model.District;
import lk.ashan.routenetlkserverapllication.module.branch.model.Province;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchstatusRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchtypeRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.DistrictRepository;
import lk.ashan.routenetlkserverapllication.module.branch.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BranchTestDataSeeder {

    @Autowired
    private BranchtypeRepository branchtypeRepository;
    @Autowired
    private BranchstatusRepository branchstatusRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    public void persistBranchType(String name) {
        Branchtype branchtype = new Branchtype();
        branchtype.setName(name);
        branchtypeRepository.save(branchtype);
    }

    public void persistBranchStatus(String name) {
        Branchstatus branchstatus = new Branchstatus();
        branchstatus.setName(name);
        branchstatusRepository.save(branchstatus);
    }

    public void persistDistrict(String name, Province province) {
        District district = new District();
        district.setName(name);
        district.setProvince(province);
        districtRepository.save(district);
    }

    public Province persistProvince(String name) {
        Province province = new Province();
        province.setName(name);
        return provinceRepository.save(province);
    }
}

