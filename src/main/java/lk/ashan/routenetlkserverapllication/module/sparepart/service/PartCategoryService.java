package lk.ashan.routenetlkserverapllication.module.sparepart.service;

import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartCategoryMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartStatusMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCategoryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partcategory;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartCategoryRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartCategoryService {

    private final PartCategoryRepository partCategoryRepository;
    private final PartCategoryMapper partCategoryMapper;

    @Transactional(readOnly = true)
    public List<PartCategoryDto> getPartCategories(){
       return partCategoryMapper.toDtoList(partCategoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Partcategory getById(Integer id) {
        return partCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found"
                ));
    }

    @Transactional(readOnly = true)
    public Partcategory getByName(String name) {
        return partCategoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found"
                ));
    }

}
