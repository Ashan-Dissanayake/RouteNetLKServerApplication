package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class BranchContextBuilder {

    public BranchContext buildForCreate(BranchCreateRequestDto dto) {
        return BranchContext.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .build();
    }

    public BranchContext buildForUpdate(BranchUpdateRequestDto dto) {
        return BranchContext.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .branchStatusId(dto.getBranchstatus() != null ? dto.getBranchstatus().getId() : null)
                .build();
    }
}
