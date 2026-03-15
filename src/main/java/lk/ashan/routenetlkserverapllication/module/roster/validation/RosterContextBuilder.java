package lk.ashan.routenetlkserverapllication.module.roster.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RosterContextBuilder {

    private final BranchRepository branchRepository;

    public RosterCreationContext buildForCreate(RosterCreateRequestDto requestDto) {

        Branch branch = branchRepository.findById(requestDto.getBranch().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + requestDto.getBranch().getId()));

        return RosterCreationContext.builder()
                .currentRosterId(null) //No id for new roster
                .branchId(branch.getId())
                .dostartofweek(requestDto.getDostartofweek())
                .doendofweek(requestDto.getDoendofweek())
                .build();
    }

    /**
     * Build context for UPDATE operation
     *
     * @param requestDto The roster update request
     * @return Validation context with currentRosterId set
     */
    public RosterCreationContext buildForUpdate(RosterUpdateRequestDto requestDto) {

        return RosterCreationContext.builder()
                .currentRosterId(requestDto.getId())  // Set for update
                .branchId(null)  // Branch cannot be changed, so no validation needed
                .dostartofweek(requestDto.getDostartofweek())
                .doendofweek(requestDto.getDoendofweek())
                .build();
    }

}
