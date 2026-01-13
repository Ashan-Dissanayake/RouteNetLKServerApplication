package lk.ashan.routenetlkserverapllication.module.roster.validation;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RosterUniquenessValidationStrategy implements RosterValidationStrategy {

    private final RosterRepository rosterRepository;

    @Override
    public void validateCreate(RosterCreateRequestDto request) {
        boolean isExisted = rosterRepository.existsByBranch_IdAndDoroster(request.getBranch().getId(),request.getDoroster());
        if (isExisted){
            throw new ResourceExistsException("Roster already existed");
        }
    }
}
