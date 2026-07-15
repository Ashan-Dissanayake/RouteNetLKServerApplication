package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchTypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchTypeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchType;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchTypeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Branch Types.
 * Provides methods to retrieve branch types and fetch branch type details by ID.
 */
@Service
@RequiredArgsConstructor
public class BranchTypeService {

    private final BranchTypeRepository branchTypeRepository;
    private final BranchTypeMapper branchTypeMapper;

    /**
     * Retrieves all branch types.
     *
     * @return a list of {@link BranchTypeDto} representing all branch types.
     */
    @Transactional(readOnly = true)
    public List<BranchTypeDto> getBranchTypes() {
        return branchTypeMapper.toDtoList(branchTypeRepository.findAll());
    }

    /**
     * Retrieves a branch type by its ID.
     *
     * @param id the ID of the branch type to retrieve.
     * @return the {@link BranchType} corresponding to the given ID.
     * @throws ResourceNotFoundException if no branch type is found with the given ID.
     */
    @Transactional(readOnly = true)
    public BranchType getById(Integer id) {
        return branchTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch type not found"
                ));
    }

}
