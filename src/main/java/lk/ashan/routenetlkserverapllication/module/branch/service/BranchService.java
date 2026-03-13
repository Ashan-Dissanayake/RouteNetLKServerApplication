package lk.ashan.routenetlkserverapllication.module.branch.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchStatusRepository;
import lk.ashan.routenetlkserverapllication.module.branch.state.BranchStateFactory;
import lk.ashan.routenetlkserverapllication.module.branch.state.BranchStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.branch.validation.BranchContext;
import lk.ashan.routenetlkserverapllication.module.branch.validation.BranchContextBuilder;
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
    private final BranchStatusService branchStatusService;
    private final BranchMapper branchMapper;

    private final BranchContextBuilder branchContextBuilder;
    private final List<BranchValidationStrategy> validationStrategies;
    private final BranchStateFactory branchStateFactory;
    private final BranchStateTransitionHandler branchStateTransitionHandler;

    @Transactional(readOnly = true)
    public List<BranchDetailResponseDto> getBranches(){
        return branchMapper.toDtoList(branchRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<BranchSummaryResponseDto> getSummaryBranches(){
        return branchMapper.toSummaryDetailList(branchRepository.findAll());
    }

    @Transactional(readOnly = true)
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

        return branchMapper.toDtoList( branchStream.collect(Collectors.toList()));
        }
        return branchMapper.toDtoList(branches);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public BranchDetailResponseDto createBranch(@NotNull BranchCreateRequestDto request) {
        BranchContext context = branchContextBuilder.buildForCreate(request);
        validationStrategies.forEach(s -> s.validateCreate(context));

        Branch branch = branchMapper.toEntity(request);

        BranchStatus initialStatus = branchStatusService.getByName(request.getBranchstatus().getName());

        branchStateFactory.getState(initialStatus.getName())
                .validateInitial();
        branch.setBranchstatus(initialStatus);

        Branch saved = branchRepository.save(branch);
        return branchMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public BranchDetailResponseDto updateBranch(@NotNull BranchUpdateRequestDto request) {
        BranchContext context = branchContextBuilder.buildForUpdate(request);
        validationStrategies.forEach(s -> s.validateUpdate(context));

        Branch existing = branchRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        branchMapper.updateEntityFromDto(request, existing);

        BranchStatus targetStatus = branchStatusService.getByName(request.getBranchstatus().getName());
        branchStateTransitionHandler.transitionTo(existing, targetStatus);

        Branch updatedBranch = branchRepository.save(existing);

        return branchMapper.toDto(updatedBranch);
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
