package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchstatusResponse;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchstatusMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchstatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchstatusService {

    private final BranchstatusRepository branchstatusRepository;
    private final BranchstatusMapper branchstatusMapper;


    public List<BranchstatusResponse> getBranchstatuses() {
        return branchstatusMapper.toBranchstatusResponseList(branchstatusRepository.findAll());
    }

}
