package lk.ashan.ntcserverapllication.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.ntcserverapllication.exception.ResourceExistsException;
import lk.ashan.ntcserverapllication.exception.ResourceNotFoundException;
import lk.ashan.ntcserverapllication.model.entity.Branch;
import lk.ashan.ntcserverapllication.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
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

    public List<Branch> searchBranch(@NotNull HashMap<String, String> params) {

        List<Branch> branches = this.branchRepository.findAll();

        if (params.isEmpty()) return branches;

        String branchname = params.get("branchname");
        String branchcode= params.get("branchcode");
        String brachstatusid= params.get("branchstatusid");

        Stream<Branch> branchStream = branches.stream();

        if(branchname!=null)branchStream = branchStream.filter(i->i.getName().contains(branchname));
        if(branchcode!=null)branchStream = branchStream.filter(i->i.getCode().equals(branchcode));
        if(brachstatusid!=null)branchStream = branchStream.filter(i->i.getBranchstatus().getId()==Integer.parseInt(brachstatusid));

        return branchStream.collect(Collectors.toList());

    }

    @Transactional
    public Branch createBranch(@NotNull Branch branch){
       validateBranchUniquenessForCreate(branch);
       return this.branchRepository.save(branch);
    }

    @Transactional
    public Branch updateBranch(@NotNull Branch branch){
        validateBranchUniquenessForUpdate(branch);
        return this.branchRepository.save(branch);
    }

    @Transactional
    public void deleteBranch(@NotNull Integer branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        branchRepository.delete(branch);
    }



    private void validateBranchUniquenessForCreate(@NotNull Branch branch) {

        if (branchRepository.existsByCode(branch.getCode())) {
            throw new ResourceExistsException("Branch code already exists.");
        }
        if (branchRepository.existsByName(branch.getName())) {
            throw new ResourceExistsException("Branch name already exists.");
        }
        if (branchRepository.existsByEmail(branch.getEmail())) {
            throw new ResourceExistsException("Branch email already exists.");
        }
        if (branchRepository.existsByTelephone(branch.getTelephone())) {
            throw new ResourceExistsException("Branch telephone already exists.");
        }
    }

    private void validateBranchUniquenessForUpdate(@NotNull Branch branch) {
        if (branchRepository.existsByCodeAndIdNot(branch.getCode(), branch.getId())) {
            throw new ResourceExistsException("Another branch already uses this code.");
        }
        if (branchRepository.existsByNameAndIdNot(branch.getName(), branch.getId())) {
            throw new ResourceExistsException("Another branch already uses this name.");
        }
        if (branchRepository.existsByEmailAndIdNot(branch.getEmail(), branch.getId())) {
            throw new ResourceExistsException("Another branch already uses this email.");
        }
        if (branchRepository.existsByTelephoneAndIdNot(branch.getTelephone(), branch.getId())) {
            throw new ResourceExistsException("Another branch already uses this telephone.");
        }
    }

}
