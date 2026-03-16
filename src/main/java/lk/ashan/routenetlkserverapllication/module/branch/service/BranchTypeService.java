package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchTypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchTypeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchTypeRepository;
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
    public List<BranchTypeDto> getBranchtypes() {
        return branchTypeMapper.toDtoList(branchTypeRepository.findAll());
    }


}
