package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchStatusMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchStatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Branch Status operations.
 * Provides methods to retrieve branch statuses by various criteria.
 */
@Service
@RequiredArgsConstructor
public class BranchStatusService {

    private final BranchStatusRepository branchStatusRepository;
    private final BranchStatusMapper branchStatusMapper;

    /**
     * Retrieves all branch statuses as a list of DTOs.
     *
     * @return a list of {@link BranchStatusDto} representing all branch statuses.
     */
    @Transactional(readOnly = true)
    public List<BranchStatusDto> getBranchStatuses() {
        return branchStatusMapper.toDtoList(branchStatusRepository.findAll());
    }

    /**
     * Retrieves a branch status by its name.
     *
     * @param name the name of the branch status to retrieve.
     * @return the {@link BranchStatus} entity matching the given name.
     * @throws ResourceNotFoundException if no branch status with the given name is found.
     */
    @Transactional(readOnly = true)
    public BranchStatus getByName(String name) {
        return branchStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch status '" + name + "' not found"
                ));
    }

    /**
     * Retrieves a branch status by its ID.
     *
     * @param id the ID of the branch status to retrieve.
     * @return the {@link BranchStatus} entity matching the given ID.
     * @throws ResourceNotFoundException if no branch status with the given ID is found.
     */
    @Transactional(readOnly = true)
    public BranchStatus getById(Integer id) {
        return branchStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch status not found"
                ));
    }
}
