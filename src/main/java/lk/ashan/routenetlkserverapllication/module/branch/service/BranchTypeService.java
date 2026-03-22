package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchTypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchTypeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchTypeService {

    private final BranchTypeRepository branchTypeRepository;
    private final BranchTypeMapper branchTypeMapper;

    @Transactional(readOnly = true)
    public List<BranchTypeDto> getBranchTypes() {
        return branchTypeMapper.toDtoList(branchTypeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public BranchType getById(Integer id) {
        return branchTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch type not found"
                ));
    }

}
