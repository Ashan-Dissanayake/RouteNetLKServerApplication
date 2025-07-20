package lk.ashan.ntcserverapllication.module.branch.service;

import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import lk.ashan.ntcserverapllication.module.branch.repository.BranchtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchtypeService {

    private final BranchtypeRepository branchtypeRepository;

    @Autowired
    public BranchtypeService(BranchtypeRepository branchtypeRepository) {
        this.branchtypeRepository = branchtypeRepository;
    }

    public List<Branchtype> getBranchtypes() {

        List<Branchtype> branchtypes = this.branchtypeRepository.findAll();

        return branchtypes.stream().map(
                branchtype -> {
                    Branchtype d = new Branchtype();
                    d.setId(branchtype.getId());
                    d.setName(branchtype.getName());
                    return d;
                }
        ).collect(Collectors.toList());
    }
}
