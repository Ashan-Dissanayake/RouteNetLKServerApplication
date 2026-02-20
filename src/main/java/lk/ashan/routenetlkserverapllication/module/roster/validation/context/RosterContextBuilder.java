package lk.ashan.routenetlkserverapllication.module.roster.validation.context;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
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

}
