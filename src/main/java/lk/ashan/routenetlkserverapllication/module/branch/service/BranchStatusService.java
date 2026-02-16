package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchstatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchstatusMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchstatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchStatusService {

    private final BranchstatusRepository branchstatusRepository;
    private final BranchstatusMapper branchstatusMapper;


    public List<BranchstatusDto> getBranchstatuses() {
        return branchstatusMapper.toDtoList(branchstatusRepository.findAll());
    }

}
