package lk.ashan.ntcserverapllication.module.branch.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.ntcserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.ntcserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.ntcserverapllication.module.branch.dto.BranchCreateRequest;
import lk.ashan.ntcserverapllication.module.branch.dto.BranchFullResponse;
import lk.ashan.ntcserverapllication.module.branch.dto.BranchUpdateRequest;
import lk.ashan.ntcserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.ntcserverapllication.module.branch.model.Branch;
import lk.ashan.ntcserverapllication.module.branch.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public List<BranchFullResponse> getBranches(){
        return branchMapper.toFulBranchResponseList(branchRepository.findAll());
    }

    public List<BranchFullResponse> searchBranch(@NotNull HashMap<String, String> params) {

        List<Branch> branches = branchRepository.findAll();

        if (!params.isEmpty()) {

        String branchname = params.get("branchname");
        String branchcode= params.get("branchcode");
        String brachstatusid= params.get("branchstatusid");

        Stream<Branch> branchStream = branches.stream();

        if(branchname!=null)branchStream = branchStream.filter(i->i.getName().contains(branchname));
        if(branchcode!=null)branchStream = branchStream.filter(i->i.getCode().equals(branchcode));
        if(brachstatusid!=null)branchStream = branchStream.filter(i->i.getBranchstatus().getId()==Integer.parseInt(brachstatusid));

        return branchMapper.toFulBranchResponseList( branchStream.collect(Collectors.toList()));

        }

        return branchMapper.toFulBranchResponseList(branches);

    }

    @Transactional
    public BranchFullResponse createBranch(@NotNull BranchCreateRequest request) {
        validateBranchUniquenessForCreate(request);
        Branch branch = branchMapper.toBranchEntity(request);
        Branch saved = branchRepository.save(branch);
        return branchMapper.toFullBranchResponse(saved);
    }


    @Transactional
    public BranchFullResponse updateBranch(@NotNull BranchUpdateRequest request) {
        validateBranchUniquenessForUpdate(request);
        Branch branch = branchMapper.toBranchEntity(request);
        Branch updated = branchRepository.save(branch);
        return branchMapper.toFullBranchResponse(updated);
    }

    @Transactional
    public void deleteBranch(@NotNull Integer branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        branchRepository.delete(branch);
    }

    private void validateBranchUniquenessForCreate(@NotNull BranchCreateRequest branch) {

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

    private void validateBranchUniquenessForUpdate(@NotNull BranchUpdateRequest branch) {
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
