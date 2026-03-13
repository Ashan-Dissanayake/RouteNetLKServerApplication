package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchStatusMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchstatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchStatusService {

    private final BranchStatusRepository branchStatusRepository;
    private final BranchStatusMapper branchStatusMapper;

    @Transactional(readOnly = true)
    public List<BranchstatusDto> getBranchStatuses() {
        return branchStatusMapper.toDtoList(branchStatusRepository.findAll());
    }

    public BranchStatus getByName(String name) {
        return branchStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch status '" + name + "' not found"
                ));
    }


}
