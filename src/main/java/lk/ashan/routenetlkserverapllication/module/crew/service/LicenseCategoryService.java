package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.LicenseCategoryMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.LicenseCategory;
import lk.ashan.routenetlkserverapllication.module.crew.repository.LicenseCategoryRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseCategoryService {

    private final LicenseCategoryRepository licenseCategoryRepository;
    private final LicenseCategoryMapper licenseCategoryMapper;

    @Transactional(readOnly = true)
    public List<LicenseCategoryDto> getLicenseCategories(){
       return licenseCategoryMapper.toDtoList(licenseCategoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public LicenseCategory getById(Integer id) {
        return licenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "License category not found"
                ));
    }

}
