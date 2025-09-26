package lk.ashan.routenetlkserverapllication.module.branch.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
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

    public List<BranchDetailResponseDto> getBranches(){
        return branchMapper.toDetailList(branchRepository.findAll());
    }

    public List<BranchDetailResponseDto> searchBranch(@NotNull HashMap<String, String> params) {

        List<Branch> branches = branchRepository.findAll();

        if (!params.isEmpty()) {

        String branchname = params.get("ssname");
        String branchcode= params.get("sscode");
        String brachstatusid= params.get("ssbranchstatus");

        Stream<Branch> branchStream = branches.stream();

        if(branchname!=null)branchStream = branchStream.filter(i->i.getName().toLowerCase().contains(branchname.toLowerCase()));
        if(branchcode!=null)branchStream = branchStream.filter(i-> i.getCode().equalsIgnoreCase(branchcode));
        if(brachstatusid!=null)branchStream = branchStream.filter(i->i.getBranchstatus().getId()==Integer.parseInt(brachstatusid));

        return branchMapper.toDetailList( branchStream.collect(Collectors.toList()));

        }

        return branchMapper.toDetailList(branches);

    }

    @Transactional
    public BranchDetailResponseDto createBranch(@NotNull BranchCreateRequestDto request) {
        validateBranchUniquenessForCreate(request);
        Branch branch = branchMapper.toEntity(request);
        branch.getBranchcoverages().forEach(c -> c.setBranch(branch));
        Branch saved = branchRepository.save(branch);
        return branchMapper.toDto(saved);
    }


    @Transactional
    public BranchDetailResponseDto updateBranch(@NotNull BranchUpdateRequestDto request) {
        validateBranchUniquenessForUpdate(request);
        Branch branch = branchMapper.toEntity(request);
        Branch updated = branchRepository.save(branch);
        return branchMapper.toDto(updated);
    }

    @Transactional
    public void deleteBranch(@NotNull Integer branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        branchRepository.delete(branch);
    }

    private void validateBranchUniquenessForCreate(@NotNull BranchCreateRequestDto branch) {

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

    private void validateBranchUniquenessForUpdate(@NotNull BranchUpdateRequestDto branch) {
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
