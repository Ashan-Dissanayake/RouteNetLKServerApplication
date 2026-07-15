package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import org.springframework.stereotype.Component;

/**
 * A builder class for creating and updating BranchContext objects.
 * This class provides methods to build BranchContext instances
 * for branch creation and update operations.
 */
@Component
public class BranchContextBuilder {

    /**
     * Builds a BranchContext object for branch creation.
     *
     * @param dto the BranchCreateRequestDto containing the branch creation details
     * @return a BranchContext object populated with the provided creation details
     */
    public BranchContext buildForCreate(BranchCreateRequestDto dto) {
        return BranchContext.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .address(dto.getAddress())
                .build();
    }

    /**
     * Builds a BranchContext object for branch update.
     *
     * @param dto the BranchUpdateRequestDto containing the branch update details
     * @return a BranchContext object populated with the provided update details
     */
    public BranchContext buildForUpdate(BranchUpdateRequestDto dto) {
        return BranchContext.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .address(dto.getAddress())
                .branchStatusId(dto.getBranchstatus() != null ? dto.getBranchstatus().getId() : null)
                .build();
    }
}
