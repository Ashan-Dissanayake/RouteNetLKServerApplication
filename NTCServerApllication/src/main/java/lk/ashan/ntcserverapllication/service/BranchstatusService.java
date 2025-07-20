package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.Branchstatus;
import lk.ashan.ntcserverapllication.repository.BranchstatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchstatusService {

    private final BranchstatusRepository branchstatusRepository;

    @Autowired
    public BranchstatusService(BranchstatusRepository branchstatusRepository) {
        this.branchstatusRepository = branchstatusRepository;
    }

    public List<Branchstatus> getBranchstatuss() {

        List<Branchstatus> branchstatuss = this.branchstatusRepository.findAll();

        return branchstatuss.stream().map(
                branchstatus -> {
                    Branchstatus d = new Branchstatus();
                    d.setId(branchstatus.getId());
                    d.setName(branchstatus.getName());
                    return d;
                }
        ).collect(Collectors.toList());
    }
}
