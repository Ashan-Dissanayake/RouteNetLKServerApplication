package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.LicenseCategoryMapper;
import lk.ashan.routenetlkserverapllication.module.crew.repository.LicenseCategoryRepository;
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
