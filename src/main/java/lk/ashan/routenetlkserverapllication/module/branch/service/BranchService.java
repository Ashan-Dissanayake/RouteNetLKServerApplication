package lk.ashan.routenetlkserverapllication.module.branch.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branchstatus;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchstatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.branch.validation.BranchValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchstatusRepository branchstatusRepository;
    private final BranchMapper branchMapper;
    private final List<BranchValidationStrategy> validationStrategies;

    public List<BranchDetailResponseDto> getBranches(){
        return branchMapper.toDtolList(branchRepository.findAll());
    }

    public List<BranchSummaryResponseDto> getSummaryBranches(){
        return branchMapper.toSummaryDetailList(branchRepository.findAll());
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

        return branchMapper.toDtolList( branchStream.collect(Collectors.toList()));

        }

        return branchMapper.toDtolList(branches);

    }

    @Transactional
    @DisableSoftDeleteFilter
    public BranchDetailResponseDto createBranch(@NotNull BranchCreateRequestDto request) {
        
        validationStrategies.forEach(s -> s.validateCreate(request));
        Branch branch = branchMapper.toEntity(request);
        Branch saved = branchRepository.save(branch);

        return branchMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public BranchDetailResponseDto updateBranch(@NotNull BranchUpdateRequestDto request) {

        validationStrategies.forEach(s -> s.validateUpdate(request));

        Branch existing = branchRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Update basic attributes using MapStruct
        branchMapper.updateEntityFromDto(request, existing);

        Branchstatus status = branchstatusRepository.findById(request.getBranchstatus().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        existing.setBranchstatus(status);

        return branchMapper.toDto(existing);
    }

    @Transactional
    public List<Integer> deactivateBranches(List<Integer> branchIds) {
        List<Branch> branches = branchRepository.findAllById(branchIds);

        if (branches.isEmpty())
            throw new ResourceNotFoundException("No branches found for the given IDs");

        branchRepository.removeAll(branchIds);

        return branches.stream() .map(Branch::getId) .collect(Collectors.toList());
    }

    @Transactional
    public List<Integer> activateBranches(List<Integer> branchIds) {
        List<Branch> branches = branchRepository.findAllById(branchIds);

        if (branches.isEmpty())
            throw new ResourceNotFoundException("No branches found for the given IDs");

        branchRepository.restoreAll(branchIds);

        return branches.stream() .map(Branch::getId) .collect(Collectors.toList());
    }

}
