package lk.ashan.routenetlkserverapllication.module.driver.service;

import lk.ashan.routenetlkserverapllication.module.driver.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.driver.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.driver.mapper.CrewStatusMapper;
import lk.ashan.routenetlkserverapllication.module.driver.mapper.LicenseCategoryMapper;
import lk.ashan.routenetlkserverapllication.module.driver.repository.CrewStatusRepository;
import lk.ashan.routenetlkserverapllication.module.driver.repository.LicenseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseCategoryService {

    private final LicenseCategoryRepository licenseCategoryRepository;
    private final LicenseCategoryMapper licenseCategoryMapper;

    public List<LicenseCategoryDto> getLicenseCategories(){
       return licenseCategoryMapper.toDtoList(licenseCategoryRepository.findAll());
    }

}
