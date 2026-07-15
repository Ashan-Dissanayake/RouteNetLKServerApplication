package lk.ashan.routenetlkserverapllication.module.branch.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import lk.ashan.routenetlkserverapllication.module.branch.state.BranchStateFactory;
import lk.ashan.routenetlkserverapllication.module.branch.state.BranchStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.branch.validation.BranchContext;
import lk.ashan.routenetlkserverapllication.module.branch.validation.BranchContextBuilder;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.FuelType;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
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

/**
 * Service class for managing Branch entities and related operations.
 * Provides methods for CRUD operations, searching, and state transitions.
 */
@Validated
@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchStatusService branchStatusService;
    private final BranchTypeService branchTypeService;
    private final RegionalOfficeService regionalOfficeService;

    private final BranchMapper branchMapper;

    private final BranchContextBuilder branchContextBuilder;
    private final List<BranchValidationStrategy> validationStrategies;
    private final BranchStateFactory branchStateFactory;
    private final BranchStateTransitionHandler branchStateTransitionHandler;

    /**
     * Retrieves all branches as detailed response DTOs.
     *
     * @return a list of {@link BranchDetailResponseDto} containing details of all branches.
     */
    @Transactional(readOnly = true)
    public List<BranchDetailResponseDto> getBranches() {
        return branchMapper.toDtoList(branchRepository.findAll());
    }

    /**
     * Retrieves all branches as summary DTOs.
     *
     * @return a list of {@link BranchSummaryDto} containing summary details of all branches.
     */
    @Transactional(readOnly = true)
    public List<BranchSummaryDto> getSummaryBranches() {
        return branchMapper.toSummaryDtolList(branchRepository.findAll());
    }

    /**
     * Retrieves a branch by its ID.
     *
     * @param id the ID of the branch to retrieve.
     * @return the {@link Branch} entity with the specified ID.
     * @throws ResourceNotFoundException if no branch is found with the given ID.
     */
    @Transactional(readOnly = true)
    public Branch getById(Integer id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found"
                ));
    }

    /**
     * Searches for branches based on the provided parameters.
     *
     * @param params a map of search parameters (e.g., branch name, code, status ID).
     * @return a list of {@link BranchDetailResponseDto} matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<BranchDetailResponseDto> searchBranch(@NotNull HashMap<String, String> params) {

        List<Branch> branches = branchRepository.findAll();

        if (!params.isEmpty()) {

            String branchname = params.get("ssname");
            String branchcode = params.get("sscode");
            String brachstatusid = params.get("ssbranchstatus");

            Stream<Branch> branchStream = branches.stream();

            if (branchname != null)
                branchStream = branchStream.filter(i -> i.getName().toLowerCase().contains(branchname.toLowerCase()));
            if (branchcode != null)
                branchStream = branchStream.filter(i -> i.getCode().equalsIgnoreCase(branchcode));
            if (brachstatusid != null)
                branchStream = branchStream.filter(i -> i.getBranchstatus().getId() == Integer.parseInt(brachstatusid));

            return branchMapper.toDtoList(branchStream.collect(Collectors.toList()));
        }
        return branchMapper.toDtoList(branches);
    }

    /**
     * Creates a new branch.
     *
     * @param request the {@link BranchCreateRequestDto} containing branch creation details.
     * @return the created branch as a {@link BranchDetailResponseDto}.
     */
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

    /**
     * Updates an existing branch.
     *
     * @param request the {@link BranchUpdateRequestDto} containing branch update details.
     * @return the updated branch as a {@link BranchDetailResponseDto}.
     * @throws ResourceNotFoundException if the branch to update is not found.
     * @throws BusinessRuleViolationException if the branch code is attempted to be changed.
     */
    @Transactional
    @DisableSoftDeleteFilter
    public BranchDetailResponseDto updateBranch(@NotNull BranchUpdateRequestDto request) {
        Branch existing = branchRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        if (!existing.getCode().equalsIgnoreCase(request.getCode())) {
            throw new BusinessRuleViolationException("Code cannot be changed");
        }

        BranchContext context = branchContextBuilder.buildForUpdate(request);
        validationStrategies.forEach(s -> s.validateUpdate(context));

        branchMapper.updateEntityFromDto(request, existing);

        if (request.getBranchstatus().getId() != null) {
            BranchStatus targetStatus = branchStatusService.getById(request.getBranchstatus().getId());
            branchStateTransitionHandler.transitionTo(existing, targetStatus);
        }

        if (request.getBranchtype().getId() != null) {
            BranchType type = branchTypeService.getById(request.getBranchtype().getId());
            existing.setBranchtype(type);
        }

        if (request.getRegionaloffice().getId() != null) {
            RegionalOffice ro = regionalOfficeService.getById(request.getRegionaloffice().getId());
            existing.setRegionaloffice(ro);
        }

        return branchMapper.toDto(existing);
    }

    /**
     * Deactivates a list of branches by their IDs.
     *
     * @param branchIds a list of branch IDs to deactivate.
     * @return a list of IDs of the deactivated branches.
     * @throws ResourceNotFoundException if no branches are found for the given IDs.
     */
    @Transactional
    public List<Integer> deactivateBranches(List<Integer> branchIds) {
        List<Branch> branches = branchRepository.findAllById(branchIds);

        if (branches.isEmpty())
            throw new ResourceNotFoundException("No branches found for the given IDs");

        branchRepository.removeAll(branchIds);

        return branches.stream().map(Branch::getId).collect(Collectors.toList());
    }

}
