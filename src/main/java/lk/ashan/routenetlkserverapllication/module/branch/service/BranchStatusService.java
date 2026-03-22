package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchStatusMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchStatusDto;
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
    public List<BranchStatusDto> getBranchStatuses() {
        return branchStatusMapper.toDtoList(branchStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public BranchStatus getByName(String name) {
        return branchStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch status '" + name + "' not found"
                ));
    }

    @Transactional(readOnly = true)
        public BranchStatus getById(Integer id) {
            return branchStatusRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Branch status not found"
                    ));
        }


}
