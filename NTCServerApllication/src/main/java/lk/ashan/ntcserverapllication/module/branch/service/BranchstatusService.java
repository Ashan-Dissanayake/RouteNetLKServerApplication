package lk.ashan.ntcserverapllication.module.branch.service;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchstatusResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.BranchstatusMapper;
import lk.ashan.ntcserverapllication.module.branch.repository.BranchstatusRepository;
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
