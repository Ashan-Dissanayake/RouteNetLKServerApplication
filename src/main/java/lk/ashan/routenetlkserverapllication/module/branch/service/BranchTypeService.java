package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchtypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchTypeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchTypeService {

    private final BranchTypeRepository branchTypeRepository;
    private final BranchTypeMapper branchtypeMapper;

    public List<BranchtypeDto> getBranchtypes() {
        return branchtypeMapper.toDtoList(branchTypeRepository.findAll());
    }
}
