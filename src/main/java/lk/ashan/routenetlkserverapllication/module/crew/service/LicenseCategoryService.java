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

/**
 * Service class for managing License Categories.
 * Provides methods to retrieve license categories and fetch a specific license category by ID.
 */
@Service
@RequiredArgsConstructor
public class LicenseCategoryService {

    private final LicenseCategoryRepository licenseCategoryRepository;
    private final LicenseCategoryMapper licenseCategoryMapper;

    /**
     * Retrieves all license categories.
     *
     * @return a list of LicenseCategoryDto objects representing all license categories.
     */
    @Transactional(readOnly = true)
    public List<LicenseCategoryDto> getLicenseCategories() {
        return licenseCategoryMapper.toDtoList(licenseCategoryRepository.findAll());
    }

    /**
     * Retrieves a license category by its ID.
     *
     * @param id the ID of the license category to retrieve.
     * @return the LicenseCategory entity corresponding to the given ID.
     * @throws ResourceNotFoundException if no license category is found with the given ID.
     */
    @Transactional(readOnly = true)
    public LicenseCategory getById(Integer id) {
        return licenseCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "License category not found"
                ));
    }

}
