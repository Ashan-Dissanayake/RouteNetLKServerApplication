package lk.ashan.ntcserverapllication.service;

import lk.ashan.ntcserverapllication.model.entity.Branch;
import lk.ashan.ntcserverapllication.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BranchService {

    private final BranchRepository branchRepository;

    @Autowired
    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public List<Branch> getBranches(){
        return this.branchRepository.findAll();
    }

    public List<Branch> searchBranch(HashMap<String, String> params) {

        List<Branch> branches = this.branchRepository.findAll();

        if (params.isEmpty()) return branches;

        String branchname = params.get("branchname");
        String branchcode= params.get("branchcode");
        String brachstatusid= params.get("brachstatusid");

        Stream<Branch> branchStream = branches.stream();

        if(branchname!=null)branchStream = branchStream.filter(i->i.getName().contains(branchname));
        if(branchcode!=null)branchStream = branchStream.filter(i->i.getCode().equals(branchcode));
        if(brachstatusid!=null)branchStream = branchStream.filter(i->i.getBranchstatus().getId()==Integer.parseInt(brachstatusid));

        return branchStream.collect(Collectors.toList());

    }



}
